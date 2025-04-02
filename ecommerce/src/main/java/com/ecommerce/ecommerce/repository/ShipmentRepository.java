package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Shipments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipments, Long> {

    Optional<Shipments> findByOrderId(Long orderId);
}
