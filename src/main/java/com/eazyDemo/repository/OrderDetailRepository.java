package com.eazyDemo.repository;

import com.eazyDemo.model.Order;
import com.eazyDemo.model.OrderDetail;
import com.eazyDemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    void deleteByOrderAndProduct(Order order, Product product);
}