package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String details;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
