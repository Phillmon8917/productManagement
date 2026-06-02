package com.store.dto;

import com.store.entity.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDtos {

    @Data
    public static class CreateProductRequest {
        @NotBlank(message = "Product name is required")
        @Size(max = 200, message = "Product name cannot exceed 200 characters")
        private String name;

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        private BigDecimal price;

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock cannot be negative")
        private Integer stock;

        private String category;
    }

    @Data
    public static class UpdateProductRequest {
        @NotBlank(message = "Product name is required")
        @Size(max = 200, message = "Product name cannot exceed 200 characters")
        private String name;

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        private BigDecimal price;

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock cannot be negative")
        private Integer stock;

        private String category;
    }

    @Data
    public static class ReviewRequest {
        @NotNull(message = "Approval decision is required")
        private Boolean approved;

        private String rejectionReason;
    }

    @Data
    public static class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private String category;
        private ProductStatus status;
        private String submittedBy;
        private String reviewedBy;
        private String rejectionReason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}