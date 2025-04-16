package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XResponseDTO.ShipmentResponseDTO;
import com.ecommerce.ecommerce.model.Shipments;
import com.ecommerce.ecommerce.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.RectangularShape;
import java.util.List;

@RestController
@RequestMapping("/shipments")
@Tag(name = "Shipments", description = "Endpoints for managing order shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }


    @Operation(summary = "List all shipments", description =
    "List all shipments available in the system")
    @GetMapping
    public ResponseEntity<List<Shipments>>getAllShipments(){
        return ResponseEntity.ok(shipmentService.getAll());
    }



    @Operation(summary = "Get shipment by ID", description =
    "Retrieves a specified shipment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "retrieves shipment successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> getById(@PathVariable Long id){
        return shipmentService.getById(id)
                .map(shipmentService::toShipmentResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Get shipment by order ID", description =
    "Retrieves a specified shipment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment retrieved successfully"),
            @ApiResponse(responseCode = "404",description = "shipment  not found")
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Shipments>getShipmentsByOrderId(@PathVariable Long orderId){
        return shipmentService.getByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "update shipment status", description =
    "updates the status of a specific shipment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "status shipment updated correctly"),
            @ApiResponse(responseCode = "400", description = "couldn't be updated shipment status")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Shipments>updatedShipmentStatus(
            @PathVariable Long id,
            @RequestParam String newStatus
    ){
        return ResponseEntity.ok(shipmentService.updateStatus(id, newStatus));
    }


    @Operation(summary = "delete a shipment" , description =
    "deletes a shipment of system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "shipment deleted correctly"),
            @ApiResponse(responseCode = "404", description = "couldn't deleted shipment")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteShipment(@PathVariable Long id){
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
