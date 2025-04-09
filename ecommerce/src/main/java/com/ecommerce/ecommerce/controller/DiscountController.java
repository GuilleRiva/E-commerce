package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Discounts;
import com.ecommerce.ecommerce.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<Discounts>>getAllDiscounts(){
        return ResponseEntity.ok(discountService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discounts>getDiscountById(@PathVariable Long id){
        return discountService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Discounts>>getActiveDiscounts(){
        return ResponseEntity.ok(discountService.getDiscountActive());
    }

    @GetMapping("/product/{productId}/active")
    public ResponseEntity<Discounts>getActiveDiscountByProduct(@PathVariable Long productId){
        return discountService.getDiscountActiveByProduct(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Discounts>saveDiscount(@RequestBody Discounts discounts){
        return ResponseEntity.ok(discountService.save(discounts));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteDiscount(@PathVariable Long id){
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
