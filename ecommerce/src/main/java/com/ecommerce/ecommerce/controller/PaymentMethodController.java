package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.PaymentMethods;
import com.ecommerce.ecommerce.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments-methods")
@Tag(name = "payment methods ", description = "Endpoints for managing available payment methods")
@SecurityRequirement(name = "bearerAuth")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Operation(summary = "List all payment methods", description =
    "List all payment methods available in the system")
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<PaymentMethods>> getAll(){
        return ResponseEntity.ok(paymentMethodService.getAll());
    }


    @Operation(summary = "Get payment methods by ID", description =
    "Retrieves a specified payment methods by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "payment methods retrieves successfully"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethods> getById(@PathVariable Long id){
        return paymentMethodService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @Operation(
            summary = "Check if a method exists by ID",
            description = "Returns true if a payment method with the specified ID exists in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existence check completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid method ID provided")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean>existsById(@PathVariable Long id){
        return ResponseEntity.ok(paymentMethodService.existsById(id));
    }


    @Operation(summary = "delete payment method", description =
    "deletes a payment method by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "payment method deleted successfully"),
            @ApiResponse(responseCode = "404", description = "couldn't deleted payment method")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        paymentMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
