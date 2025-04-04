package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shipments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime shippingDate;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

}
