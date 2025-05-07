package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@Tag(name = "category", description = "endpoints for categories management")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "List all categories", description =
    "List all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
            "List of categories getting successfully")
    })
    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
    @GetMapping
    public ResponseEntity<List<Category>>getAllCategories(){

        log.info("Request received to fetch all categories");

        List<Category> categories = categoryService.getAllCategories();

        log.info("Retrieved {} categories", categories.size());

        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @Operation(summary = "get categories by ID", description =
    "retrieves a category by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category founded"),
            @ApiResponse(responseCode = "404", description = "category not found")
    })
    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<Category>getCategoryById(@PathVariable Long id){

        log.info("Request received to fetch category with ID: {}", id);

        Category category= categoryService.getCategoryById(id);

        log.info("Category found: ID={}, Name={}", category.getId(), category.getName());

        return ResponseEntity.ok(category);
    }



    @Operation(summary = "create a new category", description =
    "creates a new category. Only users with roles ROLE_ADMIN or ROLE_SELLER " +
            "are authorized to access this endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category created successfully"),
            @ApiResponse(responseCode = "403", description = "access denied- unauthorized role"),
            @ApiResponse(responseCode = "404", description = " category couldn't be created")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @PostMapping
    public ResponseEntity<Category>createCategory(@RequestBody Category category){

        log.info("Request to create new category: {}", category.getName());

        Category created = categoryService.createCategory(category);

        log.info("Category created successfully: ID={}, Name={}",created.getId(), created.getName());

        return ResponseEntity.ok(created);
    }



    @Operation(summary = "updated category", description =
    "updated a field category available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category updated successfully"),
            @ApiResponse(responseCode = "403", description = "access denied- unauthorized role"),
            @ApiResponse(responseCode = "404", description = "category couldn't be updated")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<Category>updatedCategory(
            @Parameter(description = "Category ID to updated")
            @PathVariable Long id,
            @Parameter(description = "new status of category updated")
            @RequestBody Category category
    ){
        log.info("Request received to update category with ID: {}", id);

        Category updated= categoryService.updateCategory(id, category);

        log.info("Category updated successfully. ID. {}, New name: {}", updated.getId(), updated.getName());

        return ResponseEntity.ok(updated);
    }



    @Operation(summary = "delete category", description =
    "deletes a category by its ID. Only accessible to authorized users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "category deleted successfully "),
            @ApiResponse(responseCode = "403", description = "Access denied- unauthorized role"),
            @ApiResponse(responseCode = "404", description = "category not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCategory(@PathVariable Long id){

        log.info("Request to delete category with ID: {}", id);

        categoryService.deleteCategory(id);

        log.info("Category with ID {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }
}
