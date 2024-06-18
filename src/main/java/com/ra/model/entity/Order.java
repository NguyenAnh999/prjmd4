package com.ra.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Builder
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false, unique = true, length = 100)
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(length = 100)
    private String note;

    @Column(length = 100)
    private String receiveName;

    @Column(length = 255)
    private String receiveAddress;

    @Column(length = 15)
    private String receivePhone;


    @Column(nullable = false)
    private Date createdAt = new Date();

    private Date receivedAt;

    public enum OrderStatus {
        WAITING, CONFIRM, DELIVERY, SUCCESS, CANCEL, DENIED
    }
}