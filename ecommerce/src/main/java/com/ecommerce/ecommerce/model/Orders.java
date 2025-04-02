package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.roles.OrderStatus;
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
    @JoinColumn(name = "users_id")
    private Users users;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetails>orderDetails;

    @OneToOne(mappedBy = "orders")
    private Payments payments;

    @OneToOne(mappedBy = "orders")
    private Shipments shipments;

    @ManyToMany
    private List<Products>products;

}
