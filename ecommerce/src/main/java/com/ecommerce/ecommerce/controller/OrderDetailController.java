package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.OrderDetails;
import com.ecommerce.ecommerce.service.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/order-Details")
@Tag(name = "order details", description = "endpoints for managing individual order items")
@SecurityRequirement(name = "bearerAuth")
public class OrderDetailController {

    private final OrderDetailsService orderDetailsService;

    @Autowired
    public OrderDetailController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }



    @Operation(summary = "List all order details", description =
    "retrieves a list of all order details (individual purchase items)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list order's detail successfully"),
            @ApiResponse(responseCode = "400" , description = "invalid request or internal error retrieving order details")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER, CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<OrderDetails>>getAllOrderDetails(){
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetail());
    }



    @Operation(summary = "get order detail by ID", description =
    "retrieves a specified order detail by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order details retrieves successfully"),
            @ApiResponse(responseCode = "400", description = "couldn't be founded order's detail of purchase")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER, CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails>getOrderDetailById(@PathVariable Long id){
        return ResponseEntity.ok(orderDetailsService.getOrderDetailById(id));
    }




    @Operation(summary = "delete a order detail", description =
    "delete a order detail by ID if the necessary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "order detail deleted successfully"),
            @ApiResponse(responseCode = "404", description = "couldn't be deleted order detail by user ID")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteOrderDetail(@PathVariable Long id){
        orderDetailsService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}
