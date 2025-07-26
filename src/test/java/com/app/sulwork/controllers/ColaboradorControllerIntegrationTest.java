package com.app.sulwork.controllers;


import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.dto.UpdatedCafeDto;
import com.app.sulwork.dto.UpdatedStatusCafeDto;
import com.app.sulwork.repository.ColaboradorRepository;
import com.app.sulwork.services.ColaboradorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
class ColaboradorControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final ColaboradorService colaboradorService;

    private final ObjectMapper objectMapper;

    private final ColaboradorRepository colaboradorRepository;

    @BeforeEach
    void setup() {
        colaboradorRepository.deleteAll();
    }

    private ColaboradorDto criarColaborador() {
        return colaboradorService.createColaborador(new ColaboradorDto(
                null,
                "João da Silva",
                "12345678900",
                LocalDate.now().plusDays(1),
                List.of("Café", "Pão"),
                false
        ));
    }

    @Test
    @DisplayName("POST /colaborador/create deve criar um colaborador")
    void deveCriarColaborador() throws Exception {
        ColaboradorDto novo = new ColaboradorDto(
                null,
                "Maria Oliveira",
                "11122233344",
                LocalDate.now().plusDays(2),
                List.of("Bolo", "Suco"),
                false
        );

        mockMvc.perform(post("/colaborador/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"));
    }

    @Test
    @DisplayName("GET /colaborador/findall deve retornar lista de colaboradores")
    void deveListarTodos() throws Exception {
        criarColaborador();

        mockMvc.perform(get("/colaborador/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("PATCH /colaborador/colaborador/{id} deve atualizar colaborador")
    void deveAtualizarColaborador() throws Exception {
        ColaboradorDto salvo = criarColaborador();

        ColaboradorDto atualizado = new ColaboradorDto(
                salvo.id(),
                "João Atualizado",
                salvo.cpf(),
                salvo.dataCafe(),
                List.of("Frutas"),
                salvo.entregue()
        );

        mockMvc.perform(patch("/colaborador/colaborador/" + salvo.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"));
    }

    @Test
    @DisplayName("PATCH /colaborador/cafe/adicionar-itens/{id} deve adicionar itens e atualizar data")
    void deveAdicionarItensEAtualizarData() throws Exception {
        ColaboradorDto salvo = criarColaborador();

        UpdatedCafeDto updateDto = new UpdatedCafeDto(
                List.of("Suco", "Frutas")
        );

        mockMvc.perform(patch("/colaborador/cafe/adicionar-itens/" + salvo.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(4));
    }

    @Test
    @DisplayName("PATCH /colaborador/cafe/status/{id} deve atualizar status de entrega")
    void deveAtualizarStatusEntrega() throws Exception {
        ColaboradorDto salvo = criarColaborador();

        UpdatedStatusCafeDto statusDto = new UpdatedStatusCafeDto(true);

        mockMvc.perform(patch("/colaborador/cafe/status/" + salvo.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status de entrega atualizado com sucesso!"));
    }

    @Test
    @DisplayName("DELETE /colaborador/delete/{id} deve deletar colaborador")
    void deveDeletarColaborador() throws Exception {
        ColaboradorDto salvo = criarColaborador();

        mockMvc.perform(delete("/colaborador/delete/" + salvo.id()))
                .andExpect(status().isNoContent());
    }
}
