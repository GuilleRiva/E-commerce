package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XRequestDTO.DiscountRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.DiscountResponseDTO;
import com.ecommerce.ecommerce.model.Discounts;
import com.ecommerce.ecommerce.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@Tag(name = "discounts", description = "endpoints to discounts management")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }


    @Operation(summary = "get all discounts", description =
    "will list all discounts available in the store")
    @GetMapping
    public ResponseEntity<List<Discounts>>getAllDiscounts(){
        return ResponseEntity.ok(discountService.getAll());
    }


    @Operation(summary = "get discounts by ID", description =
    "This endpoint enable get discounts by ID available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "discounts found successfully"),
            @ApiResponse(responseCode = "404", description = "id discounts not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Discounts>getDiscountById(@PathVariable Long id){
        return discountService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "List discounts active", description =
    "List all discounts active in the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "discounts active available"),
            @ApiResponse(responseCode = "404", description = "couldn't found discounts active")
    })
    @GetMapping("/active")
    public ResponseEntity<List<Discounts>>getActiveDiscounts(){
        return ResponseEntity.ok(discountService.getDiscountActive());
    }



    @Operation(summary = "discounts active by product", description =
    "List all discounts active available in the store")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Active discount for the product found"),
            @ApiResponse(responseCode = "404", description = "No active discount for the product")
    })
    @GetMapping("/product/{productId}/active")
    public ResponseEntity<Discounts>getActiveDiscountByProduct(@PathVariable Long productId){
        return discountService.getDiscountActiveByProduct(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @Operation(summary = "save a new discount", description =
    "saves a new product fot a products. Only users with roles ROLE_ADMIN or ROLE_SELLER " +
            "can access this endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "discounts saved successfully"),
            @ApiResponse(responseCode = "404", description = "couldn't be saved the new discounts in the store")
    })
    @PostMapping
    public ResponseEntity<DiscountResponseDTO>saveDiscount(@Valid @RequestBody DiscountRequestDTO dto){
        Discounts entity = discountService.toDiscountEntity(dto);
        Discounts saved = discountService.save(entity);
        DiscountResponseDTO responseDTO = discountService.toDiscountResponseDTO(saved);
        return ResponseEntity.ok(responseDTO);
    }



    @Operation(summary = "delete discounts", description =
    "will delete a discounts available in the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "discounts deleted successfully"),
            @ApiResponse(responseCode = "400", description = "couldn't be deleted this discounts")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteDiscount(@PathVariable Long id){
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
