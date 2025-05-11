package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category>getAllCategories(){
        log.info("Fetching retrieves all categories");
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        log.info("Fetching category with ID: {}", id);

        return categoryRepository.findById(id)
                .orElseThrow(()->{
                    log.error("Category not found with ID: {}", id);
                  return  new ResourceNotFoundException("Category not found with ID:" + id);
                });
    }

    public Category createCategory(Category category){
        log.info("Creating new category with name: {}",
                category.getName());

        if (categoryRepository.existsByName(category.getName())){
            log.error("Cannot create. There is already a category with that NAME: {}",category.getName());
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory){

        log.info("Updating category with ID: {}",id);
        Category existing= categoryRepository.findById(id)
                        .orElseThrow(()->{

                        log.error("Category not found with ID: {}",id);
                        return new ResourceNotFoundException("Category not found with ID:" + id);
                        });

        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());

        Category saved = categoryRepository.save(existing);
        log.info("Category updated successfully for ID: {}", id);

        return saved;
    }

    public void deleteCategory(Long id){
        log.info("Attempting to delete category with ID: {}", id);

        if (!categoryRepository.existsById(id)){
            log.error("Cannot delete. Category not found with ID: {}", id);
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        categoryRepository.deleteById(id);
        log.info("Category with ID: {} deleted successfully", id);
    }
}
