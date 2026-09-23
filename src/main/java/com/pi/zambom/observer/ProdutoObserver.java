package com.pi.zambom.observer;

import com.pi.zambom.entity.Produto;

public interface ProdutoObserver {
    void notificar(String operacao, Produto produto);
}
