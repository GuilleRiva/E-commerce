package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.enums.PaymentType;
import com.ecommerce.ecommerce.model.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Long> {

    Optional<PaymentMethods>findByPaymentType(PaymentType paymentType);

    List<PaymentMethods> findByUser_Id(Long userId);
}
