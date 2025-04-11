package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "category", description = "endpoints for categories management")
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
    @GetMapping
    public ResponseEntity<List<Category>>getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @Operation(summary = "get categories by ID", description =
    "retrieves a category by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category founded"),
            @ApiResponse(responseCode = "404", description = "category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category>getCategoryById(@PathVariable Long id){
        Category category= categoryService.getCategoryById(id);
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
    @PostMapping
    public ResponseEntity<Category>createCategory(@RequestBody Category category){
        Category created = categoryService.createCategory(category);
        return ResponseEntity.ok(created);
    }



    @Operation(summary = "updated category", description =
    "updated a field category available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category updated successfully"),
            @ApiResponse(responseCode = "403", description = "access denied- unauthorized role"),
            @ApiResponse(responseCode = "404", description = "category couldn't be updated")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category>updatedCategory(
            @Parameter(description = "Category ID to updated")
            @PathVariable Long id,
            @Parameter(description = "new status of category updated")
            @RequestBody Category category
    ){
        Category updated= categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "delete category", description =
    "deletes a category by its ID. Only accessible to authorized users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "category deleted successfully "),
            @ApiResponse(responseCode = "403", description = "Access denied- unauthorized role"),
            @ApiResponse(responseCode = "404", description = "category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
