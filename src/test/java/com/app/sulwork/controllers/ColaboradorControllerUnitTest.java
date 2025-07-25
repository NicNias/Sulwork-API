package com.app.sulwork.controllers;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.dto.UpdatedCafeDto;
import com.app.sulwork.dto.UpdatedStatusCafeDto;
import com.app.sulwork.services.ColaboradorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ColaboradorControllerUnitTest {
    @InjectMocks
    private ColaboradorController controller;

    @Mock
    private ColaboradorService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveColaborador_retornaCreatedComDto() {
        ColaboradorDto inputDto = new ColaboradorDto(
                null, "João", "12345678900", LocalDate.now().plusDays(1), List.of("Pão"), false);

        ColaboradorDto outputDto = new ColaboradorDto(
                "id123", "João", "12345678900", LocalDate.now().plusDays(1), List.of("Pão"), false);

        when(service.createColaborador(inputDto)).thenReturn(outputDto);

        ResponseEntity<ColaboradorDto> response = controller.saveColaborador(inputDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(outputDto);

        verify(service).createColaborador(inputDto);
    }

    @Test
    void getAllColaboradores_retornaLista() {
        List<ColaboradorDto> lista = List.of(
                new ColaboradorDto("id1", "Ana", "11111111111", LocalDate.now().plusDays(1), List.of("Leite"), true));

        when(service.findAll()).thenReturn(lista);

        ResponseEntity<List<ColaboradorDto>> response = controller.getAllColaboradores();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(lista);

        verify(service).findAll();
    }

    @Test
    void updateColaborador_retornaDtoAtualizado() {
        String id = "id123";
        ColaboradorDto dto = new ColaboradorDto(id, "Maria", "22222222222", LocalDate.now().plusDays(2), List.of("Café"), false);

        when(service.updateColaborador(eq(id), any(ColaboradorDto.class))).thenReturn(dto);

        ResponseEntity<ColaboradorDto> response = controller.updateColaborador(id, dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);

        verify(service).updateColaborador(id, dto);
    }

    @Test
    void addItensAndUpdateDate_retornaDtoAtualizado() {
        String id = "id123";
        UpdatedCafeDto updatedCafeDto = new UpdatedCafeDto(List.of("Bolo", "Suco"));
        ColaboradorDto dto = new ColaboradorDto(id, "Maria", "22222222222", LocalDate.now().plusDays(2), List.of("Bolo", "Suco"), true);

        when(service.addItensAndUpdateDataCafe(eq(id), any(UpdatedCafeDto.class))).thenReturn(dto);

        ResponseEntity<ColaboradorDto> response = controller.addItensAndUpdateDate(id, updatedCafeDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);

        verify(service).addItensAndUpdateDataCafe(id, updatedCafeDto);
    }

    @Test
    void updateStatus_retornaMensagem() {
        String id = "id123";
        UpdatedStatusCafeDto updatedStatusCafeDto = new UpdatedStatusCafeDto(true);
        String mensagemEsperada = "Status atualizado com sucesso";

        when(service.UpdatedStatus(eq(id), any(UpdatedStatusCafeDto.class))).thenReturn(mensagemEsperada);

        ResponseEntity<String> response = controller.updateStatus(id, updatedStatusCafeDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mensagemEsperada);

        verify(service).UpdatedStatus(id, updatedStatusCafeDto);
    }

    @Test
    void deleteColaborador_retornaNoContent() {
        String id = "id123";

        doNothing().when(service).deleteColaborador(id);

        ResponseEntity<Void> response = controller.deleteColaborador(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(response.getBody()).isNull();

        verify(service).deleteColaborador(id);
    }
}
