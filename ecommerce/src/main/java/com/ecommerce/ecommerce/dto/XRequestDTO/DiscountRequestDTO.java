package com.ecommerce.ecommerce.dto.XRequestDTO;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequestDTO {

    @NotNull(message = "Discount percent is required ")
    @DecimalMin(value = "0.01", message = "Discount must be gather than o")
    @DecimalMax(value = "1.00", message = "Discount can't exceed 100% (1.00)")
    private BigDecimal percent;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
