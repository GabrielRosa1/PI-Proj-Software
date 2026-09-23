package com.pi.zambom.observer;

import com.pi.zambom.entity.Produto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EstoqueBaixoObserver implements ProdutoObserver{
    @Override
    public void notificar(String operacao, Produto produto) {
        if ("CREATE".equals(operacao) && produto.getQuantidade() < 10) {
            log.warn("ESTOQUE BAIXO: produto '{}' cadastrado com quantidade '{}'", produto.getNome(), produto.getQuantidade());
        }
    }
}
