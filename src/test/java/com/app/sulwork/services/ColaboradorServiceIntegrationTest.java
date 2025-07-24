package com.app.sulwork;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresAlreadyExistsEception;
import com.app.sulwork.repository.ColaboradorRepository;
import com.app.sulwork.services.ColaboradorService;
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
class ColaboradorServiceIntegrationTest {

    @Autowired
    private ColaboradorService colaboradorService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @BeforeEach
    void limpar() {
        colaboradorRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar um novo colaborador e impedir duplicatas")
    void deveSalvarENaoDuplicarColaborador() {
        // Arrange
        ColaboradorDto dto = new ColaboradorDto(
                null, "Maria", "12345678901", LocalDate.now().plusDays(1),
                List.of("Pão"), false
        );

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
}