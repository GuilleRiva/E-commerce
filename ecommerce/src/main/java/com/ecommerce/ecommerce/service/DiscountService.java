package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Discounts;
import com.ecommerce.ecommerce.repository.DiscountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    private final DiscountsRepository discountsRepository;

    @Autowired
    public DiscountService(DiscountsRepository discountsRepository) {
        this.discountsRepository = discountsRepository;
    }

    public List<Discounts>getAll(){
        return discountsRepository.findAll();
    }

    public Optional<Discounts>getById(Long id){
        return discountsRepository.findById(id);
    }

    public List<Discounts>getDiscountActive(){
        LocalDateTime now= LocalDateTime.now();
        return discountsRepository.findByEndDateBeforeAndEndDateAfter(now, now);
    }

    public Optional<Discounts> getDiscountActiveByProduct(Long productId){
        LocalDateTime now= LocalDateTime.now();
        return discountsRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now);
    }

    public Discounts save(Discounts discounts){
        return discountsRepository.save(discounts);
    }

    public void delete(Long id){
        discountsRepository.deleteById(id);
    }
}
