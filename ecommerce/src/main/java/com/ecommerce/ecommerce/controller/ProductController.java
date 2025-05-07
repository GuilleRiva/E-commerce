package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XRequestDTO.ProductResponseDTO;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@Tag(name= "Products", description ="Endpoints for product management")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    @Operation(summary = "Get all products", description =
    "List all products available in store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
            "List of products getting successfully")
    })
    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
    @GetMapping
    public ResponseEntity<List<Products>>getAllProducts(){

        log.info("Fetching all products");

        List<Products> products = productService.getAllProducts();

        log.info("Total products found: {}", products.size());

        return ResponseEntity.ok(products);
    }


    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById
            (@PathVariable Long id){
        log.info("Request retrieved to fetch product with ID:{}", id);

        Products product = productService.getProductById(id);

        log.info("Product found with ID:{}", product.getId());

        return ResponseEntity.ok(product);
    }


    @Operation(summary = "get products by price range", description =
    "List products available for a requested range price")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "List of products by range price")
    })
    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
    @GetMapping("/price-range")
    public ResponseEntity<List<Products>>getProductsByPriceRange(

            @Parameter(description = "minimum price filter ")
            @RequestParam BigDecimal min,
            @Parameter(description = "maximum price filter")
            @RequestParam BigDecimal max
            ){

        log.info("Fetching products in price range: {}, - {}", min ,max);
        List<Products> products = productService.getProductByPriceRange(min, max);

        log.info("Products found in range: {}", products.size());
        return ResponseEntity.ok(products);
    }



    @Operation(summary = "Create a new product", description =
    "This endpoint enable create a new product to the store, just users with roles: ROLE_SELLER," +
            "or ROLE_ADMIN can access to this resource")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")
    })

    @PreAuthorize("hasROLE('ADMIN')")
    @PostMapping
    public ResponseEntity<Products>createProduct(@Valid @RequestBody ProductResponseDTO dto){
        Products created= productService.saveProduct(dto);
        return ResponseEntity.ok(created);
    }



    @Operation(summary = "Update products", description =
    "Allows creating a new product. Only users with roles ROLE_SELLER " +
            "or ROLE_ADMIN are authorized to access this endpoint.")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200" , description ="Product successfully updated"),
            @ApiResponse(responseCode = "403", description = "Access denied- unauthorized role"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })

    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<Products>updateProduct(

            @PathVariable Long id,
            @RequestBody Products products
    ){
        log.info("Attempting to update product with ID: {}", id);
        Products update= productService.updateProduct(id,products);

        log.info("Product with ID {} updated successfully", id);
        return ResponseEntity.ok(update);
    }



    @Operation(summary = "delete product by id", description =
    "delete a field product by id, if necessary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
            "delete products by id")
    })

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteProducts(@PathVariable Long id){

        log.info("Attempting to delete product with ID: {}", id);
        productService.deleteProduct(id);

        log.info("Product with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
