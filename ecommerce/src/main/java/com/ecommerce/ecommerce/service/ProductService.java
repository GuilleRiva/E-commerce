package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductsRepository productsRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Products>getAllProducts(){
        return productsRepository.findAll();
    }

    public Products getProductById(Long id){
        return productsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    public List<Products>getProductByPriceRange(BigDecimal min, BigDecimal max){
        return productsRepository.findByPriceBetween(min, max);
    }

    public Products saveProduct(Products products){
        return productsRepository.save(products);
    }

    public Products updateProduct(Long id, Products updateProduct){
        Products existingProduct= getProductById(id);
        existingProduct.setName(updateProduct.getName());
        return productsRepository.save(existingProduct);
    }

    public void deleteProduct(Long id){
        if (!productsRepository.existsById(id)){
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productsRepository.deleteById(id);
    }
}
