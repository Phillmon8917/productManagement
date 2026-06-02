package com.store.service;

import com.store.dto.OrderDtos;
import com.store.entity.Order;
import com.store.entity.Product;
import com.store.entity.ProductStatus;
import com.store.repository.OrderRepository;
import com.store.repository.ProductRepository;
import com.store.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Validates stock and product availability, reduces inventory, and records the purchase.
     */
    @Transactional
    public OrderDtos.OrderResponse placeOrder(OrderDtos.PlaceOrderRequest request, UserPrincipal currentUser) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + request.getProductId()));

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new IllegalStateException("This product is not available for purchase.");
        }

        if (product.getStock() < request.getQuantity()) {
            throw new IllegalStateException(
                    "Not enough stock. Only " + product.getStock() + " unit(s) available.");
        }

        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .user(currentUser.getUser())
                .product(product)
                .quantity(request.getQuantity())
                .totalPrice(total)
                .build();

        return toResponse(orderRepository.save(order));
    }

    /**
     * Retrieves the current user's orders from newest to oldest.
     */
    public List<OrderDtos.OrderResponse> getMyOrders(UserPrincipal currentUser) {
        return orderRepository.findByUserOrderByOrderedAtDesc(currentUser.getUser())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves every recorded order for administrator review.
     */
    public List<OrderDtos.OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Copies persisted order details into the API response shape.
     */
    private OrderDtos.OrderResponse toResponse(Order order) {
        OrderDtos.OrderResponse response = new OrderDtos.OrderResponse();
        response.setId(order.getId());
        response.setProductName(order.getProduct().getName());
        response.setQuantity(order.getQuantity());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderedAt(order.getOrderedAt());
        response.setOrderedBy(order.getUser().getUsername());
        return response;
    }
}
