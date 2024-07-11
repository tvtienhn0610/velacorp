package com.eazyDemo.controller;

import com.eazyDemo.model.Product;
import com.eazyDemo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        if (productRepository.existsByName(product.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sản phẩm đã tồn tại");
        }
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sản phẩm được tạo thành công");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findByIdForUpdate(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    product.setStockQuantity(productDetails.getStockQuantity());
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword,
                                        @RequestParam int limit,
                                        @RequestParam int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return productRepository.searchProducts(keyword, pageable);
    }
}
