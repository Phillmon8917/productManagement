package com.store.controller;

import com.store.dto.ApiResponse;
import com.store.dto.ProductDtos;
import com.store.security.UserPrincipal;
import com.store.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Returns approved products for public catalog browsing.
     */
    @GetMapping("/approved")
    public ResponseEntity<ApiResponse<List<ProductDtos.ProductResponse>>> getApprovedProducts() {
        return ResponseEntity.ok(ApiResponse.success("Approved products retrieved", productService.getApprovedProducts()));
    }

    /**
     * Returns products submitted by the authenticated user.
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ProductDtos.ProductResponse>>> getMyProducts(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Your products retrieved", productService.getMyProducts(currentUser)));
    }

    /**
     * Returns the requested product details by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Product retrieved", productService.getProductById(id)));
    }

    /**
     * Submits a new product for administrator review.
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> submitProduct(
            @Valid @RequestBody ProductDtos.CreateProductRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Product submitted for review", productService.submitProduct(request, currentUser)));
    }

    /**
     * Updates product details when the authenticated user has permission.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDtos.UpdateProductRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Product updated", productService.updateProduct(id, request, currentUser)));
    }

    /**
     * Deletes a product when the authenticated user has permission.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        productService.deleteProduct(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Product deleted"));
    }
}
