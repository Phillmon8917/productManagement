package com.store.service;

import com.store.dto.ProductDtos;
import com.store.entity.Product;
import com.store.entity.ProductStatus;
import com.store.repository.ProductRepository;
import com.store.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Retrieves products that customers can currently view and purchase.
     */
    public List<ProductDtos.ProductResponse> getApprovedProducts() {
        return productRepository.findByStatus(ProductStatus.APPROVED)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves products waiting for an administrator approval decision.
     */
    public List<ProductDtos.ProductResponse> getPendingProducts() {
        return productRepository.findByStatus(ProductStatus.PENDING_APPROVAL)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves every product regardless of its review status.
     */
    public List<ProductDtos.ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Loads one product by id and converts it for API responses.
     */
    public ProductDtos.ProductResponse getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return toResponse(product);
    }

    /**
     * Saves a submitted product as pending approval for the current user.
     */
    @Transactional
    public ProductDtos.ProductResponse submitProduct(ProductDtos.CreateProductRequest request, UserPrincipal currentUser) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .status(ProductStatus.PENDING_APPROVAL)
                .submittedBy(currentUser.getUser())
                .build();

        return toResponse(productRepository.save(product));
    }

    /**
     * Applies product changes after confirming the current user can edit it.
     */
    @Transactional
    public ProductDtos.ProductResponse updateProduct(Long id, ProductDtos.UpdateProductRequest request, UserPrincipal currentUser) {
        Product product = findProductOrThrow(id);

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = product.getSubmittedBy() != null &&
                product.getSubmittedBy().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new IllegalArgumentException("You can only update products that you submitted.");
        }

        if (!isAdmin && product.getStatus() == ProductStatus.APPROVED) {
            throw new IllegalStateException("Approved products cannot be edited directly. Please contact an admin.");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());

        return toResponse(productRepository.save(product));
    }

    /**
     * Removes a product after confirming the current user owns it or is an administrator.
     */
    @Transactional
    public void deleteProduct(Long id, UserPrincipal currentUser) {
        Product product = findProductOrThrow(id);

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = product.getSubmittedBy() != null &&
                product.getSubmittedBy().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new IllegalArgumentException("You can only delete products that you submitted.");
        }

        productRepository.delete(product);
    }

    /**
     * Approves or rejects a pending product and records the reviewing administrator.
     */
    @Transactional
    public ProductDtos.ProductResponse reviewProduct(Long id, ProductDtos.ReviewRequest request, UserPrincipal reviewer) {
        Product product = findProductOrThrow(id);

        if (product.getStatus() != ProductStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("This product has already been reviewed.");
        }

        if (request.getApproved()) {
            product.setStatus(ProductStatus.APPROVED);
            product.setRejectionReason(null);
        } else {
            if (request.getRejectionReason() == null || request.getRejectionReason().isBlank()) {
                throw new IllegalArgumentException("A reason is required when rejecting a product.");
            }
            product.setStatus(ProductStatus.REJECTED);
            product.setRejectionReason(request.getRejectionReason());
        }

        product.setReviewedBy(reviewer.getUser());
        return toResponse(productRepository.save(product));
    }

    /**
     * Retrieves products submitted by the current authenticated user.
     */
    public List<ProductDtos.ProductResponse> getMyProducts(UserPrincipal currentUser) {
        return productRepository.findBySubmittedBy(currentUser.getUser())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Loads a product or fails with a message that includes the requested id.
     */
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    /**
     * Copies persisted product fields into the API response shape.
     */
    private ProductDtos.ProductResponse toResponse(Product product) {
        ProductDtos.ProductResponse response = new ProductDtos.ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategory(product.getCategory());
        response.setStatus(product.getStatus());
        response.setRejectionReason(product.getRejectionReason());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        if (product.getSubmittedBy() != null) {
            response.setSubmittedBy(product.getSubmittedBy().getUsername());
        }
        if (product.getReviewedBy() != null) {
            response.setReviewedBy(product.getReviewedBy().getUsername());
        }
        return response;
    }
}
