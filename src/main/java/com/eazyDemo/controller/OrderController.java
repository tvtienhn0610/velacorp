package com.eazyDemo.controller;

import com.eazyDemo.model.Order;
import com.eazyDemo.model.OrderDetail;
import com.eazyDemo.model.Product;
import com.eazyDemo.repository.OrderDetailRepository;
import com.eazyDemo.repository.OrderRepository;
import com.eazyDemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setCustomerName(orderDetails.getCustomerName());
                    order.setAddress(orderDetails.getAddress());
                    order.setEmail(orderDetails.getEmail());
                    order.setPhoneNumber(orderDetails.getPhoneNumber());
                    order.setStatus(orderDetails.getStatus());
                    order.setTotalAmount(orderDetails.getTotalAmount());
                    return ResponseEntity.ok(orderRepository.save(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}/add-product")
    public ResponseEntity<String> addProductToOrder(@PathVariable Long id, @RequestParam Long productId, @RequestParam int quantity) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (orderOptional.isEmpty() || productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order or Product not found");
        }

        Product product = productOptional.get();
        if (product.getStockQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient stock for product");
        }

        Order order = orderOptional.get();

        // Create or update OrderDetail
        Optional<OrderDetail> orderDetailOptional = order.getOrderDetails().stream()
                .filter(detail -> detail.getProduct().getId().equals(productId))
                .findFirst();

        OrderDetail orderDetail;
        if (orderDetailOptional.isPresent()) {
            orderDetail = orderDetailOptional.get();
            orderDetail.setQuantity(orderDetail.getQuantity() + quantity);
            orderDetail.setTotalPrice(orderDetail.getQuantity() * product.getPrice());
        } else {
            orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setTotalPrice(product.getPrice() * quantity);
            order.getOrderDetails().add(orderDetail);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);

        productRepository.save(product);
        orderRepository.save(order);
        orderDetailRepository.save(orderDetail);

        return ResponseEntity.ok("Product added to order");
    }

    @Transactional
    @PutMapping("/{id}/remove-product")
    public ResponseEntity<String> removeProductFromOrder(@PathVariable Long id, @RequestParam Long productId) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (orderOptional.isEmpty() || productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order or Product not found");
        }

        Order order = orderOptional.get();
        Product product = productOptional.get();

        Optional<OrderDetail> orderDetailOptional = order.getOrderDetails().stream()
                .filter(detail -> detail.getProduct().getId().equals(productId))
                .findFirst();

        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            product.setStockQuantity(product.getStockQuantity() + orderDetail.getQuantity());
            productRepository.save(product);
            orderDetailRepository.delete(orderDetail);
            return ResponseEntity.ok("Product removed from order");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in order");
        }
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/search")
    public List<Order> searchOrders(@RequestParam String keyword, @RequestParam Long orderId,
                                    @RequestParam int limit, @RequestParam int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return orderRepository.searchOrders(keyword, orderId, pageable);
    }
}

