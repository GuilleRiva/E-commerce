package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.PaymentMethods;
import com.ecommerce.ecommerce.repository.PaymentMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {
    private final PaymentMethodsRepository paymentMethodsRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodsRepository paymentMethodsRepository) {
        this.paymentMethodsRepository = paymentMethodsRepository;
    }

    public List<PaymentMethodsRepository> getAll(){
        return paymentMethodsRepository.findAll();
    }

    public Optional<PaymentMethodsRepository> getById(Long id){
        return paymentMethodsRepository.findById(id);
    }

    public void delete(Long id){
        paymentMethodsRepository.deleteById(id);
    }

    public boolean existsById(Long id){
        return paymentMethodsRepository.existsById(id);
    }
}
