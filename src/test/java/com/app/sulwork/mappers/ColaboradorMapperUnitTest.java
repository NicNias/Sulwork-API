package com.app.sulwork.mappers;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.entity.ColaboradorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ColaboradorMapperUnitTest {
    private ColaboradorMapper mapper;
    private ColaboradorDto dto;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ColaboradorMapper.class);

        dto = new ColaboradorDto(
                UUID.randomUUID().toString(),
                "Carlos Silva",
                "12345678901",
                LocalDate.of(2025, 7, 25),
                List.of("Bolo", "Café"),
                true
        );
    }

    @Test
    @DisplayName("Deve mapear corretamente de DTO para Entity")
    void testToModel() {

        ColaboradorEntity entity = mapper.toModel(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.nome(), entity.getNome());
        assertEquals(dto.cpf(), entity.getCpf());
        assertEquals(dto.dataCafe(), entity.getDataCafe());
        assertEquals(dto.itens(), entity.getItens());
        assertEquals(dto.entregue(), entity.isEntregue());
    }

    @Test
    @DisplayName("Deve mapear corretamente de Entity para DTO")
    void testToDto() {
        ColaboradorEntity entity = ColaboradorEntity.builder()
                .id(UUID.randomUUID().toString())
                .nome("Ana Maria")
                .cpf("98765432100")
                .dataCafe(LocalDate.now())
                .itens(List.of("Suco", "Torrada"))
                .entregue(false)
                .build();

        ColaboradorDto resultadoDto = mapper.toDto(entity);

        assertEquals(entity.getId(), resultadoDto.id());
        assertEquals(entity.getNome(), resultadoDto.nome());
        assertEquals(entity.getCpf(), resultadoDto.cpf());
        assertEquals(entity.getDataCafe(), resultadoDto.dataCafe());
        assertEquals(entity.getItens(), resultadoDto.itens());
        assertEquals(entity.isEntregue(), resultadoDto.entregue());
    }

    @Test
    @DisplayName("Deve mapear lista de entidades para lista de DTOs")
    void testListColaboradorDto() {
        List<ColaboradorEntity> entities = List.of(
                ColaboradorEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .nome("João")
                        .cpf("11111111111")
                        .dataCafe(LocalDate.now())
                        .itens(List.of("Biscoito"))
                        .entregue(true)
                        .build(),
                ColaboradorEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .nome("Maria")
                        .cpf("22222222222")
                        .dataCafe(LocalDate.now())
                        .itens(List.of("Café"))
                        .entregue(false)
                        .build()
        );

        List<ColaboradorDto> dtos = mapper.listColaboradorDto(entities);

        assertEquals(2, dtos.size());
        assertEquals("João", dtos.get(0).nome());
        assertEquals("Maria", dtos.get(1).nome());
    }

    @Test
    @DisplayName("Deve retornar null ao converter DTO null para Entity")
    void testToModelWithNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    @DisplayName("Deve retornar null ao converter Entity null para DTO")
    void testToDtoWithNull() {
        assertNull(mapper.toDto(null));
    }
}
