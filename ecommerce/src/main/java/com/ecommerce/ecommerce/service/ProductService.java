package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.ProductResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productsRepository = productsRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Products>getAllProducts(){
        log.info("Fetching retrieves all products");
        return productsRepository.findAll();
    }


    public Products getProductById(Long id){
        log.info("Fetching product with ID: {}", id);

        return productsRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Product  not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

    }


    public List<Products>getProductByPriceRange(BigDecimal min, BigDecimal max){
        log.info("Fetching products with price between {} and {}",
                min,max);

        List<Products> products = productsRepository.findByPriceBetween(min, max);
        log.info("Found {} products in the specified price range", products.size());
        return productsRepository.findByPriceBetween(min, max);
    }



    public Products saveProduct( ProductResponseDTO dto){
        log.info("Attempting to save new product: {}", dto.getName());

        Products products= new Products();
        products.setName(dto.getName());
        products.setDescription(dto.getDescription());
        products.setPrice(dto.getPrice());
        products.setStock(dto.getStock());

        Users seller= userRepository.findById(dto.getSellerId())
                .orElseThrow(()-> {
                    log.error("Seller not found with ID: {}", dto.getSellerId());
                    return new ResourceNotFoundException("Seller not found");
                });


        Category category= categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> {
                    log.error("Category not found with ID: {}", dto.getCategoryId());
                   return new ResourceNotFoundException("Category not found");
                });
        products.setCategory(category);
        products.setSELLER(seller);

        Products saved = productsRepository.save(products);
        log.info("Product saved successfully with ID: {}", saved.getId());

        return saved;
    }



    public Products updateProduct(Long id, Products updateProduct){
        log.info("Attempting to update product with ID: {}", id);

        Products existingProduct= getProductById(id);
        log.debug("Existing product before update: {}", existingProduct);

        existingProduct.setName(updateProduct.getName());
        Products savedProduct = productsRepository.save(existingProduct);
        log.info("Product with ID: {} updated successfully. New name: {}", id,savedProduct.getName());

        return savedProduct;
    }


    public ProductResponseDTO toProductResponseDTO(Products products){
        log.debug("Mapping product entity to DTO for product ID: {}", products.getId());

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setName(products.getName());
        dto.setDescription(products.getDescription());
        dto.setPrice(products.getPrice());
        dto.setStock(products.getStock());
        dto.setCategoryId(products.getCategory().getId());
        dto.setSellerId(products.getSELLER().getId());

        return dto;
    }


    public void deleteProduct(Long id){
        log.info("Attempting to delete Product with ID: {}",id);

        if (!productsRepository.existsById(id)){
            log.warn("Payment not found with ID: {}",id);
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productsRepository.deleteById(id);
        log.info("Product deleted successfully. ID: {}", id);
    }
}
