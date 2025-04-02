package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Shipments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipments, Long> {
}
