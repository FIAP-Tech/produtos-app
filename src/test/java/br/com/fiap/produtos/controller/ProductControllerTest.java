package br.com.fiap.produtos.controller;

import br.com.fiap.produtos.dto.ProductRequest;
import br.com.fiap.produtos.dto.ProductResponse;
import br.com.fiap.produtos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest(1L, "Test Product", "Test Description", 100, BigDecimal.valueOf(100.0));
        ProductResponse response = new ProductResponse(1L, "Test Product", "Test Description", 100, BigDecimal.valueOf(100.0));

        when(productService.createProduct(any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"description\":\"Test Description\",\"price\":100.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductResponse product1 = new ProductResponse(1L, "Product 1", "Description 1", 100,BigDecimal.valueOf(100.0));
        ProductResponse product2 = new ProductResponse(2L, "Product 2", "Description 2", 100,BigDecimal.valueOf(200.0));
        List<ProductResponse> productList = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].quantity").value(100))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].quantity").value(100))
                .andExpect(jsonPath("$[1].price").value(200.0));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest(1L, "Updated Product", "Updated Description", 100,BigDecimal.valueOf(150.0));
        ProductResponse response = new ProductResponse(1L, "Updated Product", "Updated Description", 100,BigDecimal.valueOf(150.0));

        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(String.valueOf(response));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated Description\",\"quantity\":\"100\",\"price\":150.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
       }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteWithoutProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
