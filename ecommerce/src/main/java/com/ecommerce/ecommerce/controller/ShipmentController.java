package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Shipments;
import com.ecommerce.ecommerce.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<Shipments>>getAllShipments(){
        return ResponseEntity.ok(shipmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipments> getById(@PathVariable Long id){
        return shipmentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Shipments>getShipmentsByOrderId(@PathVariable Long orderId){
        return shipmentService.getByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shipments>updatedShipmentStatus(
            @PathVariable Long id,
            @RequestParam String newStatus
    ){
        return ResponseEntity.ok(shipmentService.updateStatus(id, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteShipment(@PathVariable Long id){
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
