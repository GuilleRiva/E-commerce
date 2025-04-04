package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sellerId")
    private Users SELLER;

    @OneToMany(mappedBy = "products")
    private List<OrderDetails>orderDetails;

    @OneToMany(mappedBy = "products")
    private List<Discounts>discounts;

    @ManyToMany(mappedBy = "products")
    private List<Cart>carts= new ArrayList<>();

}
