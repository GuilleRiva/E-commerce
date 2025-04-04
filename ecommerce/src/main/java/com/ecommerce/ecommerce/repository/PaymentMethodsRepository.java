package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethodsRepository, Long> {

    Optional<PaymentMethodsRepository>findByName(String name);

    List<PaymentMethods> findByUserId(Long userId);
}
