package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Payments;
import com.ecommerce.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payments>> getAllPayment(){
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payments>getPaymentById(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<Payments>>getPaymentsByOrderId(@PathVariable Long orderId){
        return ResponseEntity.ok(paymentService.getPaymentsByOrderId(orderId));
    }

    @PostMapping
    public ResponseEntity<Payments>createPayment(
            @RequestParam Long orderId,
            @RequestParam Long methodId,
            @RequestParam BigDecimal amount
            ){
        return ResponseEntity.ok(paymentService.createPayment(orderId, methodId, amount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deletePayment(@PathVariable Long id){
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payments>updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String newStatus
    ){
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, newStatus));
    }

}
