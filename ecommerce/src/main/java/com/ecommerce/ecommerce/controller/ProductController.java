package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XRequestDTO.ProductResponseDTO;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name= "Products", description ="Endpoints for product management")
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
    @GetMapping
    public ResponseEntity<List<Products>>getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/{id}")
    public ResponseEntity<com.ecommerce.ecommerce.dto.XResponseDTO.ProductResponseDTO>getProductById(@PathVariable Long id){
        Products products= productService.getProductById(id);
        com.ecommerce.ecommerce.dto.XResponseDTO.ProductResponseDTO dto= productService.toProductResponseDTO(products);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "get products by price range", description =
    "List products available for a requested range price")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "List of products by range price")
    })
    @GetMapping("/price-range")
    public ResponseEntity<List<Products>>getProductsByPriceRange(
            @Parameter(description = "minimum price filter ")
            @RequestParam BigDecimal min,
            @Parameter(description = "maximum price filter")
            @RequestParam BigDecimal max
            ){
        return ResponseEntity.ok(productService.getProductByPriceRange(min, max));
    }



    @Operation(summary = "Create a new product", description =
    "This endpoint enable create a new product to the store, just users with roles: ROLE_SELLER," +
            "or ROLE_ADMIN can access to this resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")
    })
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
    @PutMapping("/{id}")
    public ResponseEntity<Products>updateProduct(
            @PathVariable Long id,
            @RequestBody Products products
    ){
        Products update= productService.updateProduct(id,products);
        return ResponseEntity.ok(update);
    }



    @Operation(summary = "delete product by id", description =
    "delete a field product by id, if necessary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
            "delete products by id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteProducts(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
