package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.OrderDetails;
import com.ecommerce.ecommerce.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/id/order-Details")
public class OrderDetailController {

    private final OrderDetailsService orderDetailsService;

    @Autowired
    public OrderDetailController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDetails>>getAllOrderDetails(){
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetail());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails>getOrderDetailById(@PathVariable Long id){
        return ResponseEntity.ok(orderDetailsService.getOrderDetailById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDetails> addOrderDetail(
            @RequestParam Long orderId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){
        OrderDetails created= orderDetailsService.addOrderDetail(orderId, productId, quantity);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteOrderDetail(@PathVariable Long id){
        orderDetailsService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}
