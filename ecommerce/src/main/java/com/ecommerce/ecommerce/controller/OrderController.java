package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XRequestDTO.OrderRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.OrderResponseDTO;
import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@Tag(name = "orders",description = "Endpoints for orders management")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @Operation(summary = "create a new order", description =
    "Creates a new purchase order from the current user's cart and selected options ")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto){

        log.info("Received request to create order: UserId={}, OrderDetail={}, PaymentMehodId={}",
                dto.getUserId(), dto.getOrderDetail(), dto.getPaymentMethodId());
        OrderResponseDTO created= orderService.createOrder(dto);

        log.info("Order created successfully with ID: {}", created.getOrderId());
        return ResponseEntity.ok(created);
    }



    @Operation(
            summary = "create a new order from the user's cart",
            description = "Generates a new order based on the current contents of the user's cart." +
                    "The cart will be cleared once the order is successfully created."
    )
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Cart is empty or invalid user ID"),
            @ApiResponse(responseCode = "404", description = "User or cart not found")
    })

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/from-cart")
    public ResponseEntity<Orders> createOrderFromCart(@RequestParam Long userId){

        log.info("Request received to create an order from cart for user ID:{}", userId);
        Orders created= orderService.createOrderFromCart(userId);

        log.info("Order successfully created from cart for user ID{}, Order ID: {}", userId,created.getId());
        return ResponseEntity.ok(created);
    }


    @Operation(summary = "Get all orders", description =
    "List all orders available in store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
            "List of orders getting successfully")
    })

    @PreAuthorize("hasAnyRole('ADMIN, SELLER, CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<Orders>>getAllOrders(){

        log.info("Request retrieved to fetch all order");
        List<Orders> orders= orderService.getAllOrders();

        log.info("Retrieved {} orders", orders.size());
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @Operation(summary = "Get order by id", description =
    "This endpoint enable get order by id available")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "found order"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })

    @PreAuthorize("hasAnyRole('ADMIN, SELLER, CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<Orders>getOrderById(@PathVariable Long id){

        log.info("Request retrieved to fetch order with ID: {}", id);
        Orders orders= orderService.getOrderById(id);

        log.info("Order found: ID={}, Order status={}",orders.getId(),orders.getOrderStatus());
        return ResponseEntity.ok(orders);
    }



    @Operation(summary = "update order status", description =
    "This endpoint will update a new order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "order status couldn't be updated")
    })

    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Orders>updateOrderStatus(

            @Parameter(description = "Order ID to update")
            @PathVariable Long id,
            @Parameter(description = "New status to apply to the orders")
            @RequestParam String newStatus
    ){
        log.info("Attempting to update order status with ID: {}", id);
        Orders updated= orderService.updateOrderStatus(id, newStatus);

        log.info("Order status with ID: {} updated successfully", id);
        return ResponseEntity.ok(updated);
    }



    @Operation(summary = "delete order", description =
    "delete a order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "order deleted successfully (no content returned)"),
            @ApiResponse(responseCode = "404", description = "this order couldn't be deleted")
    })

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteOrder(@PathVariable Long id){

        log.info("Deleting order with ID: {}",id);
        orderService.deleteOrder(id);

        log.info("order with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
