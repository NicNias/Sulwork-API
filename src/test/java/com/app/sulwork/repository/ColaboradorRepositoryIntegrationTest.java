package com.app.sulwork.repository;

import com.app.sulwork.entity.ColaboradorEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ColaboradorRepositoryIntegrationTest {
    private final ColaboradorRepository colaboradorRepository;

    @Test
    @DisplayName("Deve salvar e buscar colaborador pelo nome")
    void deveSalvarEBuscarPorNome() {
        // Arrange
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setNome("João");
        colaborador.setCpf("12345678900");
        colaborador.setDataCafe(LocalDate.now());

        colaboradorRepository.save(colaborador);

        // Act
        Optional<ColaboradorEntity> encontrado = colaboradorRepository.findByNome("João");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve buscar colaborador por CPF")
    void deveBuscarPorCpf() {
        // Arrange
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setNome("Maria");
        colaborador.setCpf("98765432100");
        colaborador.setDataCafe(LocalDate.now());

        colaboradorRepository.save(colaborador);

        // Act
        Optional<ColaboradorEntity> encontrado = colaboradorRepository.findByCpf("98765432100");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCpf()).isEqualTo("98765432100");
    }

    @Test
    @DisplayName("Deve buscar colaboradores por dataCafe e itens")
    void deveBuscarPorDataCafeEItens() {
        // Arrange
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setNome("Pedro");
        colaborador.setCpf("55555555555");
        colaborador.setDataCafe(LocalDate.of(2025, 7, 25));
        colaborador.setItens(List.of("Bolo", "Pão"));

        colaboradorRepository.save(colaborador);

        // Act
        List<ColaboradorEntity> encontrados = colaboradorRepository.findByDataCafeAndItens(
                LocalDate.of(2025, 7, 25),
                List.of("Bolo"));

        // Assert
        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.get(0).getNome()).isEqualTo("Pedro");
    }
}
