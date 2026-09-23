package com.pi.zambom.service;

import com.pi.zambom.entity.Produto;
import com.pi.zambom.observer.ProdutoObserver;
import com.pi.zambom.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final List<ProdutoObserver> observers;

    public Produto criarProduto(Produto produto){
        Produto salvo = produtoRepository.save(produto);
        notificar("CREATE", salvo);
        return salvo;
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
    }

    public void deletar(UUID id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
        notificar("DELETE", produto);
    }

    private void notificar(String operacao, Produto produto){
        for (ProdutoObserver observer : observers) {
            observer.notificar(operacao, produto);
        }
    }
}
