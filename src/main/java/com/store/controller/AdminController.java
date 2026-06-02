package com.store.controller;

import com.store.dto.ApiResponse;
import com.store.dto.OrderDtos;
import com.store.dto.ProductDtos;
import com.store.security.UserPrincipal;
import com.store.service.OrderService;
import com.store.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;

    /**
     * Returns products waiting for an administrator review decision.
     */
    @GetMapping("/products/pending")
    public ResponseEntity<ApiResponse<List<ProductDtos.ProductResponse>>> getPendingProducts() {
        return ResponseEntity.ok(ApiResponse.success("Pending products retrieved", productService.getPendingProducts()));
    }

    /**
     * Returns all products for administrator management.
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDtos.ProductResponse>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success("All products retrieved", productService.getAllProducts()));
    }

    /**
     * Records an administrator's approval or rejection decision for a product.
     */
    @PostMapping("/products/{id}/review")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> reviewProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDtos.ReviewRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Product review completed", productService.reviewProduct(id, request, currentUser)));
    }

    /**
     * Returns all orders for administrator review.
     */
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDtos.OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success("All orders retrieved", orderService.getAllOrders()));
    }
}
