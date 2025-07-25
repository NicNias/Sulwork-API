package com.app.sulwork;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.dto.UpdatedCafeDto;
import com.app.sulwork.dto.UpdatedStatusCafeDto;
import com.app.sulwork.entity.ColaboradorEntity;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresAlreadyExistsEception;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresNotFoundException;
import com.app.sulwork.exceptions.colaboradores.ItemsAlreadyRegisteredForDateException;
import com.app.sulwork.repository.ColaboradorRepository;
import com.app.sulwork.services.ColaboradorService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ColaboradorServiceIntegrationTest {

    private final ColaboradorService colaboradorService;

    private final ColaboradorRepository colaboradorRepository;

    private ColaboradorDto dto;

    @BeforeEach
    void setup() {
        colaboradorRepository.deleteAll();

        dto = new ColaboradorDto(
                null,
                "Maria",
                "12345678901",
                LocalDate.now().plusDays(1),
                List.of("Pão"),
                false
        );
    }

    @Test
    @DisplayName("Deve salvar um novo colaborador e impedir duplicatas")
    void deveSalvarENaoDuplicarColaborador() {
        // Act
        ColaboradorDto salvo = colaboradorService.createColaborador(dto);

        // Assert
        assertNotNull(salvo);
        assertEquals("Maria", salvo.nome());
        assertEquals("12345678901", salvo.cpf());

        // Act + Assert - Verifica duplicidade
        assertThrows(ColaboradoresAlreadyExistsEception.class, () -> {
            colaboradorService.createColaborador(dto);
        });
    }

    @Test
    @DisplayName("Deve retornar todos os colaboradores ou lançar exceção se lista estiver vazia")
    void deveRetornarTodos() {
        // Arrange
        ColaboradorDto salvo = colaboradorService.createColaborador(dto);

        // Act
        List<ColaboradorDto> lista = colaboradorService.findAll();

        // Assert
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("Maria", lista.get(0).nome());
        assertEquals("12345678901", lista.get(0).cpf());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver colaboradores")
    void nenhumEncontrado() {
        // Act + Assert
        assertThrows(ColaboradoresNotFoundException.class, () -> {
            colaboradorService.findAll();
        });
    }

    @Test
    @DisplayName("Deve atualizar colaborador existente com sucesso")
    void atualizarColaboradorExistente() {
        // Arrange
        ColaboradorDto salvo = colaboradorService.createColaborador(dto);

        ColaboradorDto dtoAtualizado = new ColaboradorDto(
                salvo.id(),
                "Maria Atualizada",
                "12345678902",
                LocalDate.now().plusDays(2),
                List.of("Café", "Bolo"),
                false
        );

        // Act
        ColaboradorDto atualizado = colaboradorService.updateColaborador(salvo.id(), dtoAtualizado);

        // Assert
        assertNotNull(atualizado);
        assertEquals("Maria Atualizada", atualizado.nome());
        assertEquals("12345678902", atualizado.cpf());
        assertEquals(LocalDate.now().plusDays(2), atualizado.dataCafe());
        assertEquals(List.of("Café", "Bolo"), atualizado.itens());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar colaborador inexistente")
    void atualizarInexistente() {
        ColaboradorDto dtoAtualizado = new ColaboradorDto(
                "id-invalido",
                "Maria Atualizada",
                "12345678902",
                LocalDate.now().plusDays(2),
                List.of("Café", "Bolo"),
                false
        );

        assertThrows(ColaboradoresNotFoundException.class, () -> {
            colaboradorService.updateColaborador("id-invalido", dtoAtualizado);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar com itens conflitantes na mesma data")
    void ItensConflitantes() {
        // Arrange
        ColaboradorDto colaborador1 = new ColaboradorDto(
                null,
                "João",
                "99999999999",
                LocalDate.now().plusDays(3),
                List.of("Pão", "Suco"),
                false
        );
        ColaboradorDto salvo1 = colaboradorService.createColaborador(colaborador1);

        // Arrange
        ColaboradorDto colaborador2 = new ColaboradorDto(
                null,
                "Ana",
                "88888888888",
                LocalDate.now().plusDays(4),
                List.of("Bolo"),
                false
        );
        ColaboradorDto salvo2 = colaboradorService.createColaborador(colaborador2);

        // Tenta atualizar o segundo colaborador para usar data e itens do primeiro (conflito)
        ColaboradorDto dtoConflito = new ColaboradorDto(
                salvo2.id(),
                "Ana",
                "88888888888",
                salvo1.dataCafe(),
                salvo1.itens(),
                false
        );

        assertThrows(ItemsAlreadyRegisteredForDateException.class, () -> {
            colaboradorService.updateColaborador(salvo2.id(), dtoConflito);
        });
    }

    @Test
    @DisplayName("Deve adicionar itens ao colaborador sem conflito")
    void adicionarItensSemConflito() {
        // Arrange
        ColaboradorDto salvo = colaboradorService.createColaborador(dto);

        UpdatedCafeDto atualizacao = new UpdatedCafeDto(
                List.of("Suco", "Bolo")
        );

        // Act
        ColaboradorDto atualizado = colaboradorService.addItensAndUpdateDataCafe(salvo.id(), atualizacao);

        // Assert
        assertNotNull(atualizado);
        assertEquals(3, atualizado.itens().size());
        assertTrue(atualizado.itens().containsAll(List.of("Pão", "Suco", "Bolo")));
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar itens já registrados por outro colaborador na mesma data")
    void deveLancarExcecaoPorConflitoDeItens() {
        // Arrange - colaborador 1
        colaboradorService.createColaborador(new ColaboradorDto(
                null,
                "Marcos",
                "99999999999",
                LocalDate.now().plusDays(2),
                List.of("Bolo"),
                false
        ));

        // Arrange - colaborador 2
        ColaboradorDto salvo = colaboradorService.createColaborador(new ColaboradorDto(
                null,
                "Ana",
                "88888888888",
                LocalDate.now().plusDays(2),
                List.of("Café"),
                false
        ));

        // Tentativa de adicionar item "Bolo" (já usado por Marcos na mesma data)
        UpdatedCafeDto atualizacao = new UpdatedCafeDto(
                List.of("Bolo")
        );

        // Act + Assert
        assertThrows(ItemsAlreadyRegisteredForDateException.class, () -> {
            colaboradorService.addItensAndUpdateDataCafe(salvo.id(), atualizacao);
        });
    }

    @Test
    @DisplayName("Deve atualizar o status de entrega de um colaborador existente com sucesso")
    void deveAtualizarStatusDeEntrega() {
        // Arrange
        ColaboradorDto salvo = colaboradorService.createColaborador(dto);

        UpdatedStatusCafeDto statusDto = new UpdatedStatusCafeDto(true);

        // Act
        String resultado = colaboradorService.UpdatedStatus(salvo.id(), statusDto);

        // Assert
        assertEquals("Status de entrega atualizado com sucesso!", resultado);

        ColaboradorEntity entityAtualizado = colaboradorRepository.findById(salvo.id()).orElseThrow();
        assertTrue(entityAtualizado.isEntregue());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar status de colaborador inexistente")
    void deveLancarExcecaoParaIdInvalidoNaAtualizacaoStatus() {
        UpdatedStatusCafeDto statusDto = new UpdatedStatusCafeDto(true);

        assertThrows(ColaboradoresNotFoundException.class, () -> {
            colaboradorService.UpdatedStatus("id-invalido", statusDto);
        });
    }

    @Test
    @DisplayName("Deve deletar colaborador existente com sucesso")
    void deletarColaborador() {
        // Arrange
        ColaboradorDto salvo = colaboradorService.createColaborador(dto);

        // Act
        colaboradorService.deleteColaborador(salvo.id());

        // Assert
        boolean existe = colaboradorRepository.findById(salvo.id()).isPresent();
        assertFalse(existe);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar colaborador inexistente")
    void lancarExcecaoInexistente() {
        // Act + Assert
        assertThrows(ColaboradoresNotFoundException.class, () -> {
            colaboradorService.deleteColaborador("id-invalido-qualquer");
        });
    }
}