package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails>orderDetails;

    @OneToOne(mappedBy = "order")
    private Payments payments;

    @OneToOne(mappedBy = "order")
    private Shipments shipments;

    @ManyToMany
    private List<Products>products;

}
