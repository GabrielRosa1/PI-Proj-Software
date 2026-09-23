package com.pi.zambom.service;

import com.pi.zambom.entity.Produto;
import com.pi.zambom.observer.ProdutoObserver;
import com.pi.zambom.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoObserver observer;

    private ProdutoService produtoService;

    @BeforeEach
    void setup() {
        produtoService = new ProdutoService(produtoRepository, List.of(observer));
    }

    private Produto produto(String nome, int quantidade) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao("desc");
        p.setPreco(10.0);
        p.setQuantidade(quantidade);
        return p;
    }

    @Test
    void criarSalvaENotificaCreate() {
        Produto p = produto("Caneta", 5);
        when(produtoRepository.save(p)).thenReturn(p);

        Produto salvo = produtoService.criarProduto(p);

        assertEquals("Caneta", salvo.getNome());
        verify(observer).notificar("CREATE", p);
    }

    @Test
    void listarRetornaTodos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto("A", 1), produto("B", 2)));

        assertEquals(2, produtoService.listar().size());
    }

    @Test
    void buscarPorIdEncontra() {
        UUID id = UUID.randomUUID();
        Produto p = produto("A", 1);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(p));

        assertEquals(p, produtoService.buscarPorId(id));
    }

    @Test
    void buscarPorIdInexistenteLanca404() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> produtoService.buscarPorId(id));
    }

    @Test
    void deletarRemoveENotificaDelete() {
        UUID id = UUID.randomUUID();
        Produto p = produto("A", 1);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(p));

        produtoService.deletar(id);

        verify(produtoRepository).delete(p);
        verify(observer).notificar("DELETE", p);
    }

    @Test
    void deletarInexistenteLanca404() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> produtoService.deletar(id));
        verify(produtoRepository, never()).delete(any());
        verify(observer, never()).notificar(any(), any());
    }
}