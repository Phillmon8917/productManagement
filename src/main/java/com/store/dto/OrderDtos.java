package com.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDtos {

    @Data
    public static class PlaceOrderRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "You must order at least 1 item")
        private Integer quantity;
    }

    @Data
    public static class OrderResponse {
        private Long id;
        private String productName;
        private Integer quantity;
        private BigDecimal totalPrice;
        private LocalDateTime orderedAt;
        private String orderedBy;
    }
}