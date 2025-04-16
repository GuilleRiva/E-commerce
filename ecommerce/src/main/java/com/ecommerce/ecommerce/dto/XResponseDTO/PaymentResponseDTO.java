package com.ecommerce.ecommerce.dto.XResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    private Long orderId;
    private String paymentMethodName;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

}
