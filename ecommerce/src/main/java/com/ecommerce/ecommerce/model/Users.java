package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String pass;


    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol;

    @OneToMany(mappedBy = "SELLER")
    private List<Products> products;

    @OneToMany(mappedBy = "user")
    private List<Orders>orders;

    @OneToMany(mappedBy = "user")
    private List<PaymentMethods>paymentMethods;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites= new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Cart> carts;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToOne(mappedBy = "user")
    private RefreshToken refresh_token;


}
