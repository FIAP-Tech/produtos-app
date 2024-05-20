package br.com.fiap.produtos.service;

import br.com.fiap.produtos.dto.ProductRequest;
import br.com.fiap.produtos.dto.ProductResponse;
import br.com.fiap.produtos.model.Product;
import br.com.fiap.produtos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }


    @Test
    void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest(1L, "Test Product", "Test Description", 100,BigDecimal.valueOf(100.0));
        Product product = new Product(1L, "Test Product", "Test Description", 100,BigDecimal.valueOf(100.0));

        when(productRepository.save(any())).thenReturn(product);

        ProductResponse response = productService.createProduct(productRequest);

        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertEquals(product.getQuantity(),response.quantity());
        assertEquals(product.getPrice(), response.price());

        verify(productRepository, times(1)).save(any());
    }


    @Test
    void testGetAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Test Product 1", "Test Description 1", 100,BigDecimal.valueOf(100.0)));
        productList.add(new Product(2L, "Test Product 2", "Test Description 2", 100,BigDecimal.valueOf(200.0)));

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductResponse> responseList = productService.getAllProducts();

        assertEquals(productList.size(), responseList.size());

        for (int i = 0; i < productList.size(); i++) {
            assertEquals(productList.get(i).getName(), responseList.get(i).name());
            assertEquals(productList.get(i).getDescription(), responseList.get(i).description());
            assertEquals(productList.get(i).getQuantity(), responseList.get(i).quantity());
            assertEquals(productList.get(i).getPrice(), responseList.get(i).price());
        }

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Long id = 1L;
        Product product = new Product(id, "Test Product", "Test Description", 100,BigDecimal.valueOf(100.0));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductResponse responseList = productService.getProductById(id);

        //assertEquals(1, responseList.size());
        //assertEquals(product.getName(), responseList.get(0).name());
        //assertEquals(product.getDescription(), responseList.get(0).description());
        //assertEquals(product.getQuantity(), responseList.get(0).quantity());
        //assertEquals(product.getPrice(), responseList.get(0).price());

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testGetProductByIdNotFound() {
        Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.getProductById(id));

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateProduct() {
        Long id = 1L;
        ProductRequest productRequest = new ProductRequest(id, "Updated Product","Updated Description", 100,BigDecimal.valueOf(150.0));
        Product product = new Product(id, "Original Product", "Original Description", 100,BigDecimal.valueOf(100.0));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        String response = productService.updateProduct(id, productRequest);

        assertEquals("Produto atualizado!", response);
        assertEquals(productRequest.name(), product.getName());
        assertEquals(productRequest.description(), product.getDescription());
        assertEquals(productRequest.quantity(), product.getQuantity());
        assertEquals(productRequest.price(), product.getPrice());

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(any());
    }






}
