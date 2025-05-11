package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.DiscountRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.DiscountResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Discounts;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.repository.DiscountsRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Fetching retrieves all discounts");
        return discountsRepository.findAll();
    }

    public Optional<Discounts>getById(Long id){
        log.info("Fetching discount with ID: {}",id);
        return discountsRepository.findById(id);
    }

    public List<Discounts>getDiscountActive(){

        log.info("Checking active discounts");
        LocalDateTime now= LocalDateTime.now();
        return discountsRepository.findByEndDateBeforeAndEndDateAfter(now, now);
    }


    public Discounts toDiscountEntity(DiscountRequestDTO dto){
        log.debug("Creating Discounts entity for product ID: {}, percent: {}",
                dto.getProductId(), dto.getPercent());

        Products products = productsRepository.findById(dto.getProductId())
                .orElseThrow(()->{

                    log.error("Product not found with ID: {}",dto.getProductId());
                        return new ResourceNotFoundException("Product not found with ID " + dto.getProductId());
                });

        Discounts discounts = new Discounts();
        discounts.setPercent(dto.getPercent());
        discounts.setStartDate(dto.getStartDate());
        discounts.setEndDate(dto.getEndDate());
        discounts.setProduct(products);

        return discounts;
    }


    public DiscountResponseDTO toDiscountResponseDTO ( Discounts discounts){
        log.debug("Mapping Discounts entity to DiscountResponseDTO for ID: {}",
                discounts.getId());

        return new DiscountResponseDTO(
                discounts.getId(),
                discounts.getPercent(),
                discounts.getStartDate(),
                discounts.getEndDate(),
                discounts.getProduct().getName()
        );
    }

    public Optional<Discounts> getDiscountActiveByProduct(Long productId){

        log.info("Checking active discount for product ID: {}", productId);
        LocalDateTime now= LocalDateTime.now();
        return discountsRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now);
    }


    public Discounts save(Discounts discounts){
        log.info("Saving new discount for product ID: {}",
                discounts.getProduct().getId());
        return discountsRepository.save(discounts);
    }


    public void delete(Long id){
        log.info("Deleting discount with ID: {}", id);
        discountsRepository.deleteById(id);
    }
}
