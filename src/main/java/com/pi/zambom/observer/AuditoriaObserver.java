package com.pi.zambom.observer;

import com.pi.zambom.entity.Auditoria;
import com.pi.zambom.entity.Produto;
import com.pi.zambom.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditoriaObserver implements ProdutoObserver {
    private final AuditoriaRepository auditoriaRepository;

    @Override
    public void notificar(String operacao, Produto produto) {
        Auditoria auditoria = new Auditoria();
        auditoria.setOperacao(operacao);
        auditoria.setProdutoId(produto.getId());
        auditoria.setDataHora(LocalDateTime.now());
        auditoriaRepository.save(auditoria);
    }

}
