package com.app.sulwork.services;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.dto.UpdatedCafeDto;
import com.app.sulwork.dto.UpdatedStatusCafeDto;
import com.app.sulwork.entity.ColaboradorEntity;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresAlreadyExistsEception;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresNotFoundException;
import com.app.sulwork.exceptions.colaboradores.ItemsAlreadyRegisteredForDateException;
import com.app.sulwork.mappers.ColaboradorMapper;
import com.app.sulwork.repository.ColaboradorRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ColaboradorServiceUnitTest {
    @InjectMocks
    private ColaboradorService service;

    @Mock
    private ColaboradorRepository repository;

    @Mock
    private ColaboradorMapper mapper;

    private ColaboradorDto colaboradorDto;
    private ColaboradorEntity colaboradorEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        colaboradorDto = new ColaboradorDto(
                null,
                "Ana",
                "12345678900",
                LocalDate.now().plusDays(1),
                List.of("Pão"),
                false
        );

        colaboradorEntity = new ColaboradorEntity();
        colaboradorEntity.setId("id123");
        colaboradorEntity.setNome("Ana");
        colaboradorEntity.setCpf("12345678900");
        colaboradorEntity.setDataCafe(LocalDate.now().plusDays(1));
        colaboradorEntity.setItens(List.of("Pão"));
        colaboradorEntity.setEntregue(false);
    }

    // createColaborador

    @Test
    void createColaborador_deveSalvarQuandoNaoExistirConflitos() {
        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.findByDataCafeAndItens(any(), any())).thenReturn(Collections.emptyList());
        when(mapper.toModel(colaboradorDto)).thenReturn(colaboradorEntity);
        when(mapper.toDto(colaboradorEntity)).thenReturn(colaboradorDto);

        ColaboradorDto resultado = service.createColaborador(colaboradorDto);

        assertThat(resultado).isEqualTo(colaboradorDto);
        verify(repository).save(colaboradorEntity);
    }

    @Test
    void createColaborador_deveLancarExcecaoQuandoNomeExistir() {
        when(repository.findByNome(anyString())).thenReturn(Optional.of(colaboradorEntity));

        assertThatThrownBy(() -> service.createColaborador(colaboradorDto))
                .isInstanceOf(ColaboradoresAlreadyExistsEception.class)
                .hasMessageContaining("Colaborador ja cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    void createColaborador_deveLancarExcecaoQuandoCpfExistir() {
        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.of(colaboradorEntity));

        assertThatThrownBy(() -> service.createColaborador(colaboradorDto))
                .isInstanceOf(ColaboradoresAlreadyExistsEception.class)
                .hasMessageContaining("CPF de colaborador");

        verify(repository, never()).save(any());
    }

    @Test
    void createColaborador_deveLancarExcecaoQuandoItensConflitam() {
        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.findByDataCafeAndItens(any(), any())).thenReturn(List.of(colaboradorEntity));

        assertThatThrownBy(() -> service.createColaborador(colaboradorDto))
                .isInstanceOf(ItemsAlreadyRegisteredForDateException.class)
                .hasMessageContaining("Um ou mais itens já foram cadastrados para esta data");

        verify(repository, never()).save(any());
    }

    @Test
    void createColaborador_deveCapitalizarItens() {
        ColaboradorDto dtoEntrada = new ColaboradorDto(
                null,
                "Carlos",
                "98765432100",
                LocalDate.now().plusDays(1),
                List.of("suco de limão", "bolo de chocolate"),
                false
        );

        ColaboradorEntity entityEsperado = new ColaboradorEntity();
        entityEsperado.setId("id987");
        entityEsperado.setNome("Carlos");
        entityEsperado.setCpf("98765432100");
        entityEsperado.setDataCafe(dtoEntrada.dataCafe());
        entityEsperado.setItens(List.of("Suco De Limão", "Bolo De Chocolate"));
        entityEsperado.setEntregue(false);

        ColaboradorDto dtoEsperado = new ColaboradorDto(
                "id987",
                "Carlos",
                "98765432100",
                dtoEntrada.dataCafe(),
                List.of("Suco De Limão", "Bolo De Chocolate"),
                false
        );

        when(repository.findByNome("Carlos")).thenReturn(Optional.empty());
        when(repository.findByCpf("98765432100")).thenReturn(Optional.empty());
        when(repository.findByDataCafeAndItens(dtoEntrada.dataCafe(), List.of("Suco De Limão", "Bolo De Chocolate"))).thenReturn(Collections.emptyList());
        when(mapper.toModel(any())).thenReturn(entityEsperado);
        when(mapper.toDto(entityEsperado)).thenReturn(dtoEsperado);

        ColaboradorDto resultado = service.createColaborador(dtoEntrada);

        assertThat(resultado.itens()).containsExactly("Suco De Limão", "Bolo De Chocolate");
        verify(repository).save(any());
    }

    // findAll

    @Test
    void findAll_deveRetornarListaQuandoExistir() {
        when(repository.findAll()).thenReturn(List.of(colaboradorEntity));
        when(mapper.listColaboradorDto(any())).thenReturn(List.of(colaboradorDto));

        List<ColaboradorDto> lista = service.findAll();

        assertThat(lista).isNotEmpty();
        verify(repository).findAll();
    }

    @Test
    void findAll_deveLancarExcecaoQuandoListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.findAll())
                .isInstanceOf(ColaboradoresNotFoundException.class)
                .hasMessageContaining("Nenhum Colaborador foi encontrado");

        verify(repository).findAll();
    }

    // updateColaborador

    @Test
    void updateColaborador_deveAtualizarQuandoDadosValidos() {
        String id = "id123";
        ColaboradorDto dtoAtualizado = new ColaboradorDto(
                id,
                "Ana Atualizada",
                "12345678900",
                LocalDate.now().plusDays(2),
                List.of("Café"),
                true
        );

        ColaboradorEntity entityAtualizada = new ColaboradorEntity();
        entityAtualizada.setId(id);
        entityAtualizada.setNome("Ana Atualizada");
        entityAtualizada.setCpf("12345678900");
        entityAtualizada.setDataCafe(LocalDate.now().plusDays(2));
        entityAtualizada.setItens(List.of("Café"));
        entityAtualizada.setEntregue(true);

        when(repository.findById(id)).thenReturn(Optional.of(colaboradorEntity));
        when(repository.findByDataCafeAndItens(dtoAtualizado.dataCafe(), dtoAtualizado.itens())).thenReturn(Collections.emptyList());
        when(mapper.toDto(any())).thenReturn(dtoAtualizado);

        ColaboradorDto resultado = service.updateColaborador(id, dtoAtualizado);

        assertThat(resultado).isEqualTo(dtoAtualizado);
        verify(repository).save(any(ColaboradorEntity.class));
    }

    @Test
    void updateColaborador_deveLancarExcecaoQuandoNaoEncontrar() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateColaborador("id123", colaboradorDto))
                .isInstanceOf(ColaboradoresNotFoundException.class)
                .hasMessageContaining("Nenhum Colaborador foi encontrado");
    }

    @Test
    void updateColaborador_deveLancarExcecaoQuandoItensConflitam() {
        String id = "id123";

        ColaboradorEntity conflito = new ColaboradorEntity();
        conflito.setId("id999"); // id diferente para simular conflito

        when(repository.findById(id)).thenReturn(Optional.of(colaboradorEntity));
        when(repository.findByDataCafeAndItens(colaboradorDto.dataCafe(), colaboradorDto.itens())).thenReturn(List.of(conflito));

        assertThatThrownBy(() -> service.updateColaborador(id, colaboradorDto))
                .isInstanceOf(ItemsAlreadyRegisteredForDateException.class)
                .hasMessageContaining("Um ou mais itens já foram cadastrados para esta data");
    }

    // addItensAndUpdateDataCafe

    @Test
    void addItensAndUpdateDataCafe_deveAdicionarItensSemConflito() {
        String id = "id123";
        UpdatedCafeDto updatedCafeDto = new UpdatedCafeDto(List.of("Bolo", "Suco"));

        when(repository.findById(id)).thenReturn(Optional.of(colaboradorEntity));
        when(repository.findByDataCafeAndItens(colaboradorEntity.getDataCafe(), List.of("Pão", "Bolo", "Suco"))).thenReturn(Collections.emptyList());
        when(mapper.toDto(any())).thenReturn(colaboradorDto);

        ColaboradorDto resultado = service.addItensAndUpdateDataCafe(id, updatedCafeDto);

        assertThat(resultado).isEqualTo(colaboradorDto);
        verify(repository).save(any(ColaboradorEntity.class));
    }

    @Test
    void addItensAndUpdateDataCafe_deveLancarExcecaoQuandoConflito() {
        String id = "id123";
        UpdatedCafeDto updatedCafeDto = new UpdatedCafeDto(List.of("Bolo"));

        ColaboradorEntity conflito = new ColaboradorEntity();
        conflito.setId("id999");

        when(repository.findById(id)).thenReturn(Optional.of(colaboradorEntity));
        when(repository.findByDataCafeAndItens(colaboradorEntity.getDataCafe(), List.of("Pão", "Bolo"))).thenReturn(List.of(conflito));

        assertThatThrownBy(() -> service.addItensAndUpdateDataCafe(id, updatedCafeDto))
                .isInstanceOf(ItemsAlreadyRegisteredForDateException.class)
                .hasMessageContaining("Um ou mais itens já foram cadastrados para esta data");
    }

    @Test
    void addItensAndUpdateDataCafe_deveLancarExcecaoQuandoNaoEncontrarColaborador() {
        String id = "id123";
        UpdatedCafeDto updatedCafeDto = new UpdatedCafeDto(List.of("Bolo"));

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addItensAndUpdateDataCafe(id, updatedCafeDto))
                .isInstanceOf(ColaboradoresNotFoundException.class)
                .hasMessageContaining("Nenhum Colaborador foi encontrado");
    }

    // UpdatedStatus

    @Test
    void UpdatedStatus_deveAtualizarStatus() {
        String id = "id123";
        UpdatedStatusCafeDto updatedStatusCafeDto = new UpdatedStatusCafeDto(true);

        when(repository.findById(id)).thenReturn(Optional.of(colaboradorEntity));
        when(mapper.toDto(any())).thenReturn(colaboradorDto);

        String resultado = service.updatedStatus(id, updatedStatusCafeDto);

        assertThat(resultado).isEqualTo("Status de entrega atualizado com sucesso!");
        verify(repository).save(any(ColaboradorEntity.class));
    }

    @Test
    void UpdatedStatus_deveLancarExcecaoQuandoNaoEncontrar() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        ThrowingCallable executable = () -> service.updatedStatus("id123", new UpdatedStatusCafeDto(true));

        assertThatThrownBy(executable)
                .isInstanceOf(ColaboradoresNotFoundException.class)
                .hasMessageContaining("Nenhum Colaborador foi encontrado");
    }

    // deleteColaborador

    @Test
    void deleteColaborador_deveDeletarQuandoEncontrar() {
        when(repository.findById(anyString())).thenReturn(Optional.of(colaboradorEntity));
        doNothing().when(repository).delete(colaboradorEntity);

        service.deleteColaborador("id123");

        verify(repository).delete(colaboradorEntity);
    }

    @Test
    void deleteColaborador_deveLancarExcecaoQuandoNaoEncontrar() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteColaborador("id123"))
                .isInstanceOf(ColaboradoresNotFoundException.class)
                .hasMessageContaining("Nenhum Colaborador foi encontrado");
    }
}
