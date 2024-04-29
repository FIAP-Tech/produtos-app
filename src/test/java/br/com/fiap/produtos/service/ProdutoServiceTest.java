package br.com.fiap.produtos.service;

import br.com.fiap.produtos.model.Produto;
import br.com.fiap.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private Produtoservice produtoservice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListarProduto() {

        List<Produto> produtos = new ArrayList<>();

        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Tênis Reebok");
        produto1.setDescricao("Super confortável");
        produto1.setQuantidade_estoque(10);
        produto1.setPreco(50.0);

        produtos.add(produto1);

        when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> produtosRecebidos = produtoservice.listarProduto();

        assertEquals(1, produtosRecebidos.size());
        assertEquals("Tênis Reebok", produtosRecebidos.get(0).getNome());
        assertEquals("Super confortável", produtosRecebidos.get(0).getDescricao());
        assertEquals(10, produtosRecebidos.get(0).getQuantidade_estoque());
        assertEquals(50.0, produtosRecebidos.get(0).getPreco());

        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    public void testCadastrarProduto() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Tênis Reebok");
        produto.setDescricao("Super confortável");
        produto.setQuantidade_estoque(10);
        produto.setPreco(50.0);

        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoservice.cadastrarProduto(produto);

        assertEquals("Tênis Reebok", resultado.getNome());
        assertEquals("Super confortável", resultado.getDescricao());
        assertEquals(10, resultado.getQuantidade_estoque());
        assertEquals(50.0, resultado.getPreco());


        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void testListarUmProduto() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Tênis Reebok");
        produto.setDescricao("Super confortável");
        produto.setQuantidade_estoque(10);
        produto.setPreco(50.0);


        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));

        ResponseEntity<?> resultado = produtoservice.listarUmProduto(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produto, resultado.getBody());

        verify(produtoRepository, times(1)).findById(1);
    }

    @Test
    public void testListarUmProdutoNaoEncontrado() {
        when(produtoRepository.findAll()).thenReturn(new ArrayList<>());

        List<Produto> produtos = produtoservice.listarProduto();

        // Verificando se a lista retornada está vazia
        assertEquals(0, produtos.size());

        // Verificando se o método findAll foi chamado exatamente uma vez
        verify(produtoRepository, times(1)).findAll();


    }




    @Test
    public void testAtualizarProduto() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1);
        produtoExistente.setNome("Tênis Reebok");
        produtoExistente.setDescricao("Super confortável");
        produtoExistente.setQuantidade_estoque(10);
        produtoExistente.setPreco(50.0);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1);
        produtoAtualizado.setNome("Tênis Reebok");
        produtoAtualizado.setDescricao("Nova tecnologia para corredores");
        produtoAtualizado.setQuantidade_estoque(10);
        produtoAtualizado.setPreco(800.0);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(produtoAtualizado)).thenReturn(produtoAtualizado);

        Produto resultado = produtoservice.atualizarProduto(1, produtoAtualizado);

        assertEquals("Tênis Reebok", resultado.getNome());
        assertEquals("Nova tecnologia para corredores", resultado.getDescricao());
        assertEquals(10, resultado.getQuantidade_estoque());
        assertEquals(800.0, resultado.getPreco());

        verify(produtoRepository, times(1)).findById(1);
        verify(produtoRepository, times(1)).save(produtoAtualizado);
    }


    @Test
    public void testAtualizarProdutoProdutoNaoEncontrado() {
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            produtoservice.atualizarProduto(1, new Produto());
        });

        verify(produtoRepository, times(1)).findById(1);
        verify(produtoRepository, never()).save(any());


    }


    @Test
    public void testExcluirProduto() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1);
        produtoExistente.setNome("Tênis Reebok");
        produtoExistente.setDescricao("Super confortável");
        produtoExistente.setQuantidade_estoque(10);
        produtoExistente.setPreco(50.0);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoExistente));

        produtoservice.excluirProduto(1);

        verify(produtoRepository, times(1)).findById(1);
        verify(produtoRepository, times(1)).delete(produtoExistente);
    }



    @Test
    public void testExcluirProdutoProdutoNaoEncontrado() {
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            produtoservice.excluirProduto(1);
        });

        verify(produtoRepository, times(1)).findById(1);
        verify(produtoRepository, never()).deleteById(any());
    }


}



