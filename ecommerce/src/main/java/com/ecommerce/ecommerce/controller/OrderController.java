package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Orders>createOrder(@RequestBody Orders orders){
        Orders created= orderService.createOrder(orders);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Orders>>getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders>getOrderById(@PathVariable Long id){
        Orders orders= orderService.getOrderById(id);
        return ResponseEntity.ok(orders);
    }

   /* @GetMapping("/user/{users_id}")
    public ResponseEntity<List<Orders>>getOrdersByUserId(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrdersByUserId(users_id));
    }*/

    @PutMapping("/{id}/status")
    public ResponseEntity<Orders>updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String newStatus
    ){
        Orders updated= orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
