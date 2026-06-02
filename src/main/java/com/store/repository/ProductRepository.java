package com.store.repository;

import com.store.entity.Product;
import com.store.entity.ProductStatus;
import com.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products matching the requested review status.
     */
    List<Product> findByStatus(ProductStatus status);

    /**
     * Finds products submitted by the given user.
     */
    List<Product> findBySubmittedBy(User user);

    /**
     * Finds products matching both the requested review status and category.
     */
    List<Product> findByStatusAndCategory(ProductStatus status, String category);
}
