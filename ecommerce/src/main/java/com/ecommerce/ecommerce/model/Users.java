package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.roles.Rol;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "users")
    private List<Orders>orders;

    @OneToMany(mappedBy = "users")
    private List<PaymentMethods>paymentMethods;

    @OneToMany(mappedBy = "users")
    private List<Address> addresses;

    @OneToMany(mappedBy = "users")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "users")
    private List<Cart> carts;

    @OneToMany(mappedBy = "users")
    private List<Review> reviews;

}
