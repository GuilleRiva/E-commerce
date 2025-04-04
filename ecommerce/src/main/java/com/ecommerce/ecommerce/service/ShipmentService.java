package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.enums.ShippingStatus;
import com.ecommerce.ecommerce.model.Shipments;
import com.ecommerce.ecommerce.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<Shipments>getAll(){
        return shipmentRepository.findAll();
    }

    public Optional<Shipments>getById(Long id){
        return shipmentRepository.findById(id);
    }

    public Optional<Shipments>getByOrderId(Long orderId){
        return shipmentRepository.findByOrderId(orderId);
    }

    public Shipments save(Shipments shipments){
        return shipmentRepository.save(shipments);
    }

    public void delete(Long id){
        shipmentRepository.deleteById(id);
    }

   public Shipments updateStatus(Long shipmentId, String newStatus){
        Shipments shipments=shipmentRepository.findById(shipmentId)
                .orElseThrow(()-> new RuntimeException("Shipment not found "));
        shipments.setShippingStatus(ShippingStatus.valueOf(newStatus.toUpperCase()));
        return shipmentRepository.save(shipments);
    }

}
