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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColaboradorService {
    private final ColaboradorMapper colaboradorMapper;
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorDto createColaborador(ColaboradorDto colaboradorDto) {
        colaboradorRepository.findByNome(colaboradorDto.nome()).ifPresent(c -> {
            throw new ColaboradoresAlreadyExistsEception("Colaborador já cadastrado");
        });

        colaboradorRepository.findByCpf(colaboradorDto.cpf()).ifPresent(c -> {
            throw new ColaboradoresAlreadyExistsEception("CPF de colaborador já cadastrado");
        });

        List<String> itensFormatados = capitalizarItens(colaboradorDto.itens());

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(
                colaboradorDto.dataCafe(), itensFormatados);

        if (!conflitos.isEmpty()) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        ColaboradorDto dtoComItensFormatados = new ColaboradorDto(
                colaboradorDto.id(),
                colaboradorDto.nome(),
                colaboradorDto.cpf(),
                colaboradorDto.dataCafe(),
                itensFormatados,
                colaboradorDto.entregue()
        );

        ColaboradorEntity colaboradorNew = colaboradorMapper.toModel(dtoComItensFormatados);
        colaboradorRepository.save(colaboradorNew);

        return colaboradorMapper.toDto(colaboradorNew);
    }

    @Transactional(readOnly = true)
    public List<ColaboradorDto> findAll() {
        List<ColaboradorEntity> colaboradores = colaboradorRepository.findAll();
        if (colaboradores.isEmpty()) {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        }

        return colaboradorMapper.listColaboradorDto(colaboradores);
    }

    public ColaboradorDto updateColaborador(String id, ColaboradorDto colaboradorDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!"));

        List<String> itensFormatados = capitalizarItens(colaboradorDto.itens());

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(
                colaboradorDto.dataCafe(), itensFormatados);

        boolean temConflito = conflitos.stream().anyMatch(c -> !c.getId().equals(id));
        if (temConflito) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        colaborador.setNome(colaboradorDto.nome());
        colaborador.setCpf(colaboradorDto.cpf());
        colaborador.setDataCafe(colaboradorDto.dataCafe());
        colaborador.setItens(itensFormatados);

        colaboradorRepository.save(colaborador);

        return colaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public ColaboradorDto addItensAndUpdateDataCafe(String id, UpdatedCafeDto updatedCafeDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!"));

        Set<String> itensAtuais = new HashSet<>(colaborador.getItens());
        Set<String> novosItens = new HashSet<>(capitalizarItens(updatedCafeDto.itens()));
        itensAtuais.addAll(novosItens);

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(
                colaborador.getDataCafe(), new ArrayList<>(itensAtuais));

        boolean temConflito = conflitos.stream().anyMatch(c -> !c.getId().equals(id));
        if (temConflito) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        colaborador.setItens(new ArrayList<>(itensAtuais));

        colaboradorRepository.save(colaborador);

        return colaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public String updatedStatus(String id, UpdatedStatusCafeDto updatedStatusCafeDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!"));

        colaborador.setEntregue(updatedStatusCafeDto.entregue());
        colaboradorRepository.save(colaborador);

        return "Status de entrega atualizado com sucesso!";
    }

    public void deleteColaborador(String id) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!"));

        colaboradorRepository.delete(colaborador);
    }

    private List<String> capitalizarItens(List<String> itens) {
        if (itens == null) return Collections.emptyList();

        return itens.stream()
                .map(item -> Arrays.stream(item.trim().split("\\s+"))
                        .map(palavra -> palavra.isEmpty() ? palavra :
                                palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase())
                        .collect(Collectors.joining(" "))
                )
                .toList();
    }
}