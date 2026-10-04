package com.cjvaldi.springcloud.msvc.products.repository;

import com.cjvaldi.libs.msvc.commons.entities.Product;
import com.cjvaldi.springcloud.msvc.products.repositories.ProductRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Debe persistir y recuperar un producto usando MySQL en Testcontainers")
    void shouldSaveAndFindProduct() {
        // Given
        Product product = new Product();
        product.setName("Teclado Mecanico");
        product.setPrice(120.00);

        // When
        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Teclado Mecanico");
        assertThat(found.get().getPrice()).isEqualTo(120.00);
    }
}