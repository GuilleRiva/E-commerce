package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XResponseDTO.ShipmentResponseDTO;
import com.ecommerce.ecommerce.enums.ShippingStatus;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Shipments;
import com.ecommerce.ecommerce.repository.ShipmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<Shipments>getAll(){
        log.info("Fetching retrieves all shipments");
        return shipmentRepository.findAll();
    }

    public Optional<Shipments>getById(Long id){
        log.info("Fetching by ID:{}", id);
        return shipmentRepository.findById(id);
    }


    public ShipmentResponseDTO toShipmentResponseDTO( Shipments shipments){
        log.debug("Mapping shipment entity to DTO for shipment ID: {}", shipments.getId());

        return new ShipmentResponseDTO(
                shipments.getId(),
                shipments.getTrackingCode(),
                shipments.getShippingStatus(),
                shipments.getOrder()
        );
    }


    public Optional<Shipments>getByOrderId(Long orderId){
        log.info("Fetching by order ID: {}", orderId);
        return shipmentRepository.findByOrderId(orderId);
    }


    public Shipments save(Shipments shipments){
        log.info("Attempting to save new shipment for order ID: {}", shipments.getOrder().getId());

        Shipments saved = shipmentRepository.save(shipments);
        log.info("Shipment saved successfully with ID: {}", saved.getId());
        return saved;
    }

    public void delete(Long id){
        log.info("Attempting to delete shipment with ID: {}",id);

        if (!shipmentRepository.existsById(id)){
            log.warn("Shipment not found with ID: {}", id);
            throw new ResourceNotFoundException("Shipment not found with ID." + id);
        }
        shipmentRepository.deleteById(id);
        log.info("Shipment deleted successfully with ID : {}", id);
    }

   public Shipments updateStatus(Long shipmentId, String newStatus){
        log.info("Attempting to update status. Shipment ID: {}, newStatus {}", shipmentId, newStatus);

        Shipments shipments=shipmentRepository.findById(shipmentId)
                .orElseThrow(()-> {
                    log.error("Shipment not found with ID: {}", shipmentId);
                         return new RuntimeException("Shipment not found ");
                });

        shipments.setShippingStatus(ShippingStatus.valueOf(newStatus.toUpperCase()));
        log.info("Shipment status updated successfully. newStatus :{}", newStatus);
        return shipmentRepository.save(shipments);

    }

}
