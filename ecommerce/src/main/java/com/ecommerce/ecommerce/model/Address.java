package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String areaCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
