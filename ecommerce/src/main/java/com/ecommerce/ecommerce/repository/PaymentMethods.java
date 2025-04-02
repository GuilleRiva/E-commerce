package com.ecommerce.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethods extends JpaRepository<PaymentMethods, Long> {

    Optional<PaymentMethods>findByName(String name);
}
