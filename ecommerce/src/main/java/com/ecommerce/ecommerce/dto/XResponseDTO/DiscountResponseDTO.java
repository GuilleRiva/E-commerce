package com.ecommerce.ecommerce.dto.XResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponseDTO {
    private Long id;
    private BigDecimal percent;
    private LocalDateTime startDate;
    private LocalDateTime endTime;
    private String productName;

}
