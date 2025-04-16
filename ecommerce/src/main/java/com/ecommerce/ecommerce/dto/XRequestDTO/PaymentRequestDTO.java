package com.ecommerce.ecommerce.dto.XRequestDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Payment method ID is required")
    private Long paymentMethod;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "")
    private BigDecimal amount;

}
