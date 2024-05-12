package br.com.fiap.produtos.service;

import br.com.fiap.produtos.dto.ProductRequest;
import br.com.fiap.produtos.dto.ProductResponse;
import br.com.fiap.produtos.model.Product;
import br.com.fiap.produtos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Produto cadastrado!");
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }


    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
    }
}
