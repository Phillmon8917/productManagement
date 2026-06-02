package com.store.config;

import com.store.entity.*;
import com.store.repository.ProductRepository;
import com.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Seeds the default users and sample products when the application starts.
     */
    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    /**
     * Creates the initial admin and sample customer accounts when they are missing.
     */
    private void seedUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@store.com")
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("john")) {
            User john = User.builder()
                    .username("john")
                    .password(passwordEncoder.encode("john123"))
                    .email("john@example.com")
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .build();
            userRepository.save(john);
        }

        if (!userRepository.existsByUsername("jane")) {
            User jane = User.builder()
                    .username("jane")
                    .password(passwordEncoder.encode("jane123"))
                    .email("jane@example.com")
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .build();
            userRepository.save(jane);
        }
    }

    /**
     * Adds sample approved products once, after confirming the catalog is empty.
     */
    private void seedProducts() {
        if (productRepository.count() > 0) return;

        User admin = userRepository.findByUsername("admin").orElseThrow();

        productRepository.save(Product.builder()
                .name("Wireless Headphones")
                .description("Premium noise-cancelling over-ear headphones with 30-hour battery life.")
                .price(new BigDecimal("129.99"))
                .stock(50)
                .category("Electronics")
                .status(ProductStatus.APPROVED)
                .submittedBy(admin)
                .reviewedBy(admin)
                .build());

        productRepository.save(Product.builder()
                .name("Mechanical Keyboard")
                .description("Full-size mechanical keyboard with Cherry MX switches and RGB backlighting.")
                .price(new BigDecimal("89.99"))
                .stock(30)
                .category("Electronics")
                .status(ProductStatus.APPROVED)
                .submittedBy(admin)
                .reviewedBy(admin)
                .build());

        productRepository.save(Product.builder()
                .name("Running Shoes")
                .description("Lightweight breathable running shoes with responsive foam cushioning.")
                .price(new BigDecimal("74.99"))
                .stock(100)
                .category("Sports")
                .status(ProductStatus.APPROVED)
                .submittedBy(admin)
                .reviewedBy(admin)
                .build());

        productRepository.save(Product.builder()
                .name("Coffee Maker")
                .description("12-cup programmable coffee maker with built-in grinder and thermal carafe.")
                .price(new BigDecimal("59.99"))
                .stock(25)
                .category("Kitchen")
                .status(ProductStatus.APPROVED)
                .submittedBy(admin)
                .reviewedBy(admin)
                .build());

        productRepository.save(Product.builder()
                .name("Yoga Mat")
                .description("Eco-friendly non-slip yoga mat, 6mm thick with carrying strap.")
                .price(new BigDecimal("34.99"))
                .stock(75)
                .category("Sports")
                .status(ProductStatus.APPROVED)
                .submittedBy(admin)
                .reviewedBy(admin)
                .build());
    }
}
