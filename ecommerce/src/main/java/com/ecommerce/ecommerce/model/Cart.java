package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToMany
    @JoinTable(name = "cart_id")
    private List<Products> products= new ArrayList<>();

}
