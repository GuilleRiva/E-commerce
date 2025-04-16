package com.ecommerce.ecommerce.dto.XResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Long orderId;
    private String status;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private List<OrderItemDTO> items;
}
