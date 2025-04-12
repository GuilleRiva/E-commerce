package com.ecommerce.ecommerce.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(example = "2025-01-10T23:29:44.758")
    private LocalDateTime timestamp;

    @Schema(example = "Product not found with ID: 12")
    private String message;

    @Schema(example = "404")
    private int status;

    @Schema(example = "/api/products/12")
    private String path;

    public ErrorResponse(LocalDateTime now, int value, String description) {
    }
}
