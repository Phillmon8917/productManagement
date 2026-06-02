package com.store.controller;

import com.store.dto.ApiResponse;
import com.store.dto.OrderDtos;
import com.store.security.UserPrincipal;
import com.store.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Places an order for the authenticated user.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDtos.OrderResponse>> placeOrder(
            @Valid @RequestBody OrderDtos.PlaceOrderRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Order placed successfully", orderService.placeOrder(request, currentUser)));
    }

    /**
     * Returns the authenticated user's order history.
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderDtos.OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Your order history retrieved", orderService.getMyOrders(currentUser)));
    }
}
