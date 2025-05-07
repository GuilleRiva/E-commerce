package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Payments;
import com.ecommerce.ecommerce.service.PaymentService;
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

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payments", description = "Endpoint to payments management")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }



    @Operation(summary = "List all payments", description =
    "List all payments made in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list payments successfully"),
            @ApiResponse(responseCode = "400", description = "the list of payments don't available")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Payments>> getAllPayment(){

        log.info("Request to fetch all payments");

        List<Payments> payments = paymentService.getAllPayments();

        log.info("Retrieved {} payments", payments.size());

        return ResponseEntity.ok(payments);
    }




    @Operation(summary = "get payments by ID", description =
    "Retrieves a specified payment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "payments retrieves successfully"),
            @ApiResponse(responseCode = "400", description = "couldn't found payment ID")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<Payments>getPaymentById(@PathVariable Long id){

        log.info("Fetching payments with ID: {}", id);

        Payments payments = paymentService.getPaymentById(id);

        log.info("Retrieved payments ID={}, Amount={}",
                payments.getId(), payments.getAmount());

        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }




    @Operation(
            summary = "Get all payments for a specific order",
            description = "Retrieves the list of payments associated with the given order ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieves successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found or has no payments")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payments>>getPaymentsByOrderId(@PathVariable Long orderId){

        log.info("Fetching payments for order ID:{}", orderId);

        List<Payments>payments = paymentService.getPaymentsByOrderId(orderId);

        log.info("Found {} payments for order ID {}", payments.size(), orderId);

        return ResponseEntity.ok(payments);
    }




    @Operation(summary = "create a payment",description =
    "Creates and registers a new payment for an existing order using the specified payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment made successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order ID, method ID or amount"),
            @ApiResponse(responseCode = "404", description = "Order or payment method not found.")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Payments>createPayment(

            @Parameter(description = "ID of the user to associate the payment with", required = true)
            @RequestParam Long orderId,

            @Parameter(description = "ID of the payment method", required = true)
            @RequestParam Long methodId,

            @Parameter(description = "Total amount of the payment to register", required = true)
            @RequestParam BigDecimal amount
            ){
        log.info("Creating payment for order ID: {}, method ID: {}, amount> {}",
                orderId,methodId,amount);

        Payments payments = paymentService.createPayment(orderId, methodId, amount);

        log.info("Payment created: ID={}, OrderID={}, Amount={}",
                payments.getId(), payments.getOrder(), payments.getAmount());

        return ResponseEntity.ok(payments);
    }



    @Operation(summary = "deleted a payment", description =
    "delete a payment made by the customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Couldn't deleted the payment")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deletePayment(@PathVariable Long id){

        log.info("Deleting payment with ID: {}", id);

            paymentService.deletePayment(id);

            log.info("Payment with ID {} deleted successfully", id);

            return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Update status of payment", description =
    "Retrieves updated status of the payments by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "status payment updated correctly"),
            @ApiResponse(responseCode = "400", description = "Couldn't be updated the status payment")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Payments>updatePaymentStatus(

            @Parameter(description = "ID of the payment status to check", required = true)
            @PathVariable Long id,

            @Parameter(description = "Status of payment to check", required = true)
            @RequestParam String newStatus
    ){
        log.info("Updating payment status for ID {} to {}", id, newStatus);

        Payments updated = paymentService.updatePaymentStatus(id, newStatus);

        log.info("Updated payment status: ID={}, NewStatus={}", id, updated.getOrderStatus());

        return ResponseEntity.ok(updated);
    }

}
