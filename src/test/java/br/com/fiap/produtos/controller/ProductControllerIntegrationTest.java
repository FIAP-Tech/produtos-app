package br.com.fiap.produtos.controller;

import br.com.fiap.produtos.dto.ProductRequest;
import br.com.fiap.produtos.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ProductControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllProducts() {
        Product[] produtos = restTemplate.getForObject("/api/produtos", Product[].class);
        assertThat(produtos.length).isGreaterThan(1);
        assertThat(produtos).isNotEmpty();
    }

    @Test
    void shouldFindProductIsOK() {
        ResponseEntity<Product> response = restTemplate.exchange("/api/produtos/1", HttpMethod.GET, null, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldFindProductNotFound() {
        ResponseEntity<Product> response = restTemplate.exchange("/api/produtos/888", HttpMethod.GET, null, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Rollback
    void shouldCreateNewProductWhenIsValid() {
        Product produto = new Product(1L,"Android_14","Ótimo Produto!", BigDecimal.valueOf(50.0));

        ResponseEntity<Product> response = restTemplate.exchange("/api/produtos", HttpMethod.POST, new HttpEntity<Product>(produto), Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(1L);
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Android_14");
        assertThat(response.getBody().getDescription()).isEqualTo("Ótimo Produto!");
        assertThat(response.getBody().getPrice()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        Long productId = 1L;
        ProductRequest productRequest = new ProductRequest(1L, "Android_14","Ótimo produto",BigDecimal.valueOf(50.0));

        HttpHeaders headers = new HttpHeaders();
        // Add any required headers

        RestTemplate restTemplate = new RestTemplate();
        String updateUrl = "http://localhost:8081/api/produtos/" + productId;

        // Act
        ResponseEntity<String> response = restTemplate.exchange(updateUrl, HttpMethod.PUT,
                new HttpEntity<>(productRequest, headers), String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        Long productId = 1L;

        RestTemplate restTemplate = new RestTemplate();
        String deleteUrl = "http://localhost:8081/api/produtos/" + productId;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify if the product was actually deleted (you may implement this based on your application logic)
        try {
            restTemplate.getForEntity(deleteUrl, Void.class);
        } catch (HttpClientErrorException.NotFound e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }



}
