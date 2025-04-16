package com.ecommerce.ecommerce.dto.XResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private Long userId;

}
