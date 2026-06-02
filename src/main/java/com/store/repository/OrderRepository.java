package com.store.repository;

import com.store.entity.Order;
import com.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds orders placed by the given user.
     */
    List<Order> findByUser(User user);

    /**
     * Finds orders placed by the given user from newest to oldest.
     */
    List<Order> findByUserOrderByOrderedAtDesc(User user);
}
