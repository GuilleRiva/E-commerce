package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    List<Payments>findByOrderId(Long orderId);
}
