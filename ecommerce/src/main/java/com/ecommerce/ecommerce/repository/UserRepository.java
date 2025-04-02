package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Products, Long> {

    Optional<Users>findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Users>findByUsername(String username);


}
