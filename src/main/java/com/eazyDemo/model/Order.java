package com.eazyDemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderDate;
    @NotEmpty(message = "name must not be empty")
    @Size(max = 10, min = 5)
    private String customerName;
    @NotEmpty(message = "address must not be empty")
    @Size(max = 500, min = 50)
    private String address;
    private String email;
    private String phoneNumber;
    private String status;
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails;

}

