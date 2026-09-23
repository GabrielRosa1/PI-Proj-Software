package com.pi.zambom.controller;

import com.pi.zambom.repository.AuditoriaRepository;
import com.pi.zambom.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProdutoControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        auditoriaRepository.deleteAll();
        produtoRepository.deleteAll();
    }

    @Test
    void postCriaProdutoERegistraAuditoria() throws Exception {
        String json = """
                {"nome": "Caneta", "descricao": "Azul", "preco": 2.5, "quantidade": 5}
                """;

        mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Caneta"))
                .andExpect(jsonPath("$.quantidade").value(5));

        assertEquals(1, produtoRepository.count());
        assertEquals(1, auditoriaRepository.count());   // o observer de auditoria gravou o CREATE
    }
}