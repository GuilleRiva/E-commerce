package com.ecommerce.ecommerce.dto.XResponseDTO;

import com.ecommerce.ecommerce.enums.OrderStatus;
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

    public OrderResponseDTO(Long id, Long id1, List<OrderItemDTO> items, BigDecimal total, OrderStatus orderStatus, LocalDateTime createdAt) {
    }
}
