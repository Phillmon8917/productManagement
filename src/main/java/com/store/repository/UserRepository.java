package com.store.repository;

import com.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user account by username when it exists.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a username is already assigned to an account.
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether an email address is already assigned to an account.
     */
    boolean existsByEmail(String email);
}
