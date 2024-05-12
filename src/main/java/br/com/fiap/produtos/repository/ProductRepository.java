package br.com.fiap.produtos.repository;

import br.com.fiap.produtos.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
