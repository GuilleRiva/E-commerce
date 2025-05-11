package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.PaymentMethods;
import com.ecommerce.ecommerce.repository.PaymentMethodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentMethodService {
    private final PaymentMethodsRepository paymentMethodsRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodsRepository paymentMethodsRepository) {
        this.paymentMethodsRepository = paymentMethodsRepository;
    }

    public List<PaymentMethods> getAll(){
        log.info("Fetching retrieves all payment methods");
        return paymentMethodsRepository.findAll();
    }

    public Optional<PaymentMethods> getById(Long id){
        log.info("Fetching payment methods with ID: {}", id);
        return paymentMethodsRepository.findById(id);
    }

    public void delete(Long id){
        log.info("Attempting to delete payment method with ID: {}", id);

        paymentMethodsRepository.deleteById(id);
        log.info("Payment method deleted successfully. ID: {}", id);
    }

    public boolean existsById(Long id){
        log.info("Checking existence of payment method with ID: {}", id);

        boolean exists = paymentMethodsRepository.existsById(id);

        if (exists){
            log.info("Payment method with ID: {} exists", id);
        }else {
            log.warn("Payment method with ID: {} does not exist", id);
        }
        return exists;
    }
}
