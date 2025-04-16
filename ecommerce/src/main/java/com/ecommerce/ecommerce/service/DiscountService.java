package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.DiscountRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.DiscountResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Discounts;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.repository.DiscountsRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    private final DiscountsRepository discountsRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public DiscountService(DiscountsRepository discountsRepository, ProductsRepository productsRepository) {
        this.discountsRepository = discountsRepository;
        this.productsRepository = productsRepository;
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


    public Discounts toDiscountEntity(DiscountRequestDTO dto){
        Products products = productsRepository.findById(dto.getProductId())
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID " + dto.getProductId()));

        Discounts discounts = new Discounts();
        discounts.setPercent(dto.getPercent());
        discounts.setStartDate(dto.getStartDate());
        discounts.setEndDate(dto.getEndDate());
        discounts.setProduct(products);
        return discounts;
    }


    public DiscountResponseDTO toDiscountResponseDTO ( Discounts discounts){
        return new DiscountResponseDTO(
                discounts.getId(),
                discounts.getPercent(),
                discounts.getStartDate(),
                discounts.getEndDate(),
                discounts.getProduct().getName()
        );
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
