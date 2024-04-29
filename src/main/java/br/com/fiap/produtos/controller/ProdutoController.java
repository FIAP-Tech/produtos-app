package br.com.fiap.produtos.controller;

import br.com.fiap.produtos.model.Produto;
import br.com.fiap.produtos.service.Produtoservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private Produtoservice produtoservice;

    @GetMapping
    public List<Produto> listarProduto() {
        return produtoservice.listarProduto();
    }

    @PostMapping
    public Produto cadastrarProduto(@RequestBody Produto produto) {
        return produtoservice.cadastrarProduto(produto);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<?> listarUmProduto(@PathVariable Integer produtoId) {
        return produtoservice.listarUmProduto(produtoId);
    }

    @PutMapping("/{produtoId}")
    public Produto atualizarProduto(@PathVariable Integer produtoId, @RequestBody Produto novoProduto) {
        return produtoservice.atualizarProduto(produtoId, novoProduto);
    }

    @DeleteMapping("/{produtoId}")
    public void excluirProduto(@PathVariable Integer produtoId) {
        produtoservice.excluirProduto(produtoId);
    }


}
