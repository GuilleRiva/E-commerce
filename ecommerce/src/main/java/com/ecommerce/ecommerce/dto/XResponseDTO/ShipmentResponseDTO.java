package com.ecommerce.ecommerce.dto.XResponseDTO;

import com.ecommerce.ecommerce.enums.ShippingStatus;
import com.ecommerce.ecommerce.model.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponseDTO {

    private Long id;
    private String status;
    private String trackingCode;
    private LocalDateTime estimatedDeliveryDate;
    private Long orderId;



    public ShipmentResponseDTO(Long id, String trackingCode, ShippingStatus shippingStatus, Orders order) {
    }
}
