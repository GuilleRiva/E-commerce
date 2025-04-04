package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category>getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with ID:" + id));
    }

    public Category createCategory(Category category){
        if (categoryRepository.existsByName(category.getName())){
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory){
        Category existing= getCategoryById(id);
        existing.setName(updatedCategory.getName());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id){
        if (!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
