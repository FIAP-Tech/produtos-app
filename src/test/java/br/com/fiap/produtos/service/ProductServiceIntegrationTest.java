package br.com.fiap.produtos.service;

import br.com.fiap.produtos.dto.ProductRequest;
import br.com.fiap.produtos.dto.ProductResponse;
import br.com.fiap.produtos.model.Product;
import br.com.fiap.produtos.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testCreateProduct() {
        // Given
        ProductRequest productRequest = new ProductRequest(1L, "Test Product", "Test Description", BigDecimal.valueOf(100.0));

        // When
        ProductResponse response = productService.createProduct(productRequest);

        // Then
        assertNotNull(response.name());
        assertEquals(productRequest.name(), response.name());
        assertEquals(productRequest.description(), response.description());
        assertEquals(productRequest.price(), response.price());

        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        assertEquals(productRequest.name(), products.get(0).getName());
        assertEquals(productRequest.description(), products.get(0).getDescription());
        assertEquals(productRequest.price(), products.get(0).getPrice());
    }

    @Test
    void testGetAllProducts() {
        // Given
        productRepository.save(new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0)));
        productRepository.save(new Product(2L, "Product 2", "Description 2", BigDecimal.valueOf(200.0)));

        // When
        List<ProductResponse> responseList = productService.getAllProducts();

        // Then
        assertEquals(2, responseList.size());
    }

    @Test
    void testGetProductById() {
        // Given
        Product product = productRepository.save(new Product(1L, "Test Product", "Test Description", BigDecimal.valueOf(100.0)));

        // When
        List<ProductResponse> responseList = productService.getProductById(product.getId());

        // Then
        assertEquals(1, responseList.size());
        assertEquals(product.getName(), responseList.get(0).name());
        assertEquals(product.getDescription(), responseList.get(0).description());
        assertEquals(product.getPrice(), responseList.get(0).price());
    }



    @Test
    void testUpdateProduct() {
        // Given
        Product product = productRepository.save(new Product(1L, "Original Product", "Original Description", BigDecimal.valueOf(100.0)));
        Long id = product.getId();
        ProductRequest productRequest = new ProductRequest(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(150.0));

        // When
        String response = productService.updateProduct(id, productRequest);

        // Then
        assertEquals("Produto atualizado!", response);
        Optional<Product> updatedProductOptional = productRepository.findById(id);
        assertTrue(updatedProductOptional.isPresent());
        Product updatedProduct = updatedProductOptional.get();
        assertEquals(productRequest.name(), updatedProduct.getName());
        assertEquals(productRequest.description(), updatedProduct.getDescription());
        assertEquals(productRequest.price(), updatedProduct.getPrice());
    }

    @Test
    void testDeleteProduct() {
        // Given
        Product product = productRepository.save(new Product(1L, "Test Product", "Test Description", BigDecimal.valueOf(100.0)));
        Long id = product.getId();

        // When
        productService.deleteProduct(id);

        // Then
        assertFalse(productRepository.findById(id).isPresent());
    }


}
