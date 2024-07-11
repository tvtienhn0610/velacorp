package com.eazyDemo.repository;

import com.eazyDemo.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm kiếm đơn hàng dựa trên tên khách hàng hoặc ID đơn hàng với phân trang
    @Query("SELECT o FROM Order o WHERE o.customerName LIKE %:keyword% OR o.id = :orderId")
    List<Order> searchOrders(@Param("keyword") String keyword, @Param("orderId") Long orderId, Pageable pageable);
}