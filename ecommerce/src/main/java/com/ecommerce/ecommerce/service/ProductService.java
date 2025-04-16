package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.ProductResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
        return productsRepository.findAll();
    }


    public Products getProductById(Long id){
        return productsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID: " + id));
    }


    public List<Products>getProductByPriceRange(BigDecimal min, BigDecimal max){
        return productsRepository.findByPriceBetween(min, max);
    }



    public Products saveProduct( ProductResponseDTO dto){
        Users seller= userRepository.findById(dto.getSellerId())
                .orElseThrow(()-> new ResourceNotFoundException("Seller not found"));

        Category category= categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category not found"));

        Products products= new Products();
        products.setName(dto.getName());
        products.setDescription(dto.getDescription());
        products.setPrice(dto.getPrice());
        products.setStock(dto.getStock());
        products.setSELLER(seller);
        products.setCategory(category);

        return productsRepository.save(products);
    }



    public Products updateProduct(Long id, Products updateProduct){
        Products existingProduct= getProductById(id);
        existingProduct.setName(updateProduct.getName());
        return productsRepository.save(existingProduct);
    }


    public com.ecommerce.ecommerce.dto.XResponseDTO.ProductResponseDTO toProductResponseDTO(Products products){
        return new com.ecommerce.ecommerce.dto.XResponseDTO.ProductResponseDTO(
                products.getId(),
                products.getName(),
                products.getDescription(),
                products.getPrice(),
                products.getStock(),
                products.getCategory().getName(),
                products.getSELLER().getUsername()
        );
    }

    public void deleteProduct(Long id){
        if (!productsRepository.existsById(id)){
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productsRepository.deleteById(id);
    }
}
