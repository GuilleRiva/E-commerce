package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.service.ProductService;
import com.ecommerce.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Products>>getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Products>>getProductsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max
            ){
        return ResponseEntity.ok(productService.getProductByPriceRange(min, max));
    }

    @PostMapping
    public ResponseEntity<Products>createProduct(@RequestBody Products products){
        Products created= productService.saveProduct(products);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Products>updateProduct(
            @PathVariable Long id,
            @PathVariable Products products
    ){
        Products update= productService.updateProduct(id,products);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteProducts(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
