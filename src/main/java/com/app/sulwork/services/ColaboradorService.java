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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ColaboradorService {
    private final ColaboradorMapper colaboradorMapper;
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorDto createColaborador(ColaboradorDto colaboradorDto) {
        colaboradorRepository.findByNome(colaboradorDto.nome()).ifPresent(colaboradorEntity -> {
            throw new ColaboradoresAlreadyExistsEception("Colaborador ja cadastrado");
        });

        colaboradorRepository.findByCpf(colaboradorDto.cpf()).ifPresent(colaboradorEntity -> {
            throw new ColaboradoresAlreadyExistsEception("CPF de colaborador ja cadastrado");
        });

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(
                colaboradorDto.dataCafe(),
                colaboradorDto.itens()
        );

        if (!conflitos.isEmpty()) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        ColaboradorEntity colaboradorNew = colaboradorMapper.toModel(colaboradorDto);

        colaboradorRepository.save(colaboradorNew);

        return colaboradorMapper.toDto(colaboradorNew);
    }

    @Transactional(readOnly = true)
    public List<ColaboradorDto> findAll() {
        List<ColaboradorEntity> colaboradores = colaboradorRepository.findAll();
        if (colaboradores.isEmpty()) {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        }

        return colaboradorMapper.ListColaboradorDto(colaboradores);
    }

    public ColaboradorDto updateColaborador(String id, ColaboradorDto colaboradorDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(colaboradorDto.dataCafe(), colaboradorDto.itens());
        boolean temConflito = conflitos.stream().anyMatch(c -> !c.getId().equals(id));

        if (temConflito) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        colaborador.setNome(colaboradorDto.nome());
        colaborador.setCpf(colaboradorDto.cpf());
        colaborador.setDataCafe(colaboradorDto.dataCafe());
        colaborador.setItens(colaboradorDto.itens());

        colaboradorRepository.save(colaborador);

        return colaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public ColaboradorDto addItensAndUpdateDataCafe(String id, UpdatedCafeDto updatedCafeDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });

        Set<String> itensAtuais = new HashSet<>(colaborador.getItens());
        itensAtuais.addAll(updatedCafeDto.itens());

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(colaborador.getDataCafe(), new ArrayList<>(itensAtuais));
        boolean temConflito = conflitos.stream().anyMatch(c -> !c.getId().equals(id));

        if (temConflito) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        colaborador.setItens(new ArrayList<>(itensAtuais));

        colaboradorRepository.save(colaborador);

        return colaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public String UpdatedStatus(String id, UpdatedStatusCafeDto updatedStatusCafeDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });

        colaborador.setEntregue(updatedStatusCafeDto.entregue());

        colaboradorRepository.save(colaborador);

        return "Status de entrega atualizado com sucesso!";
    }

    public void deleteColaborador(String id) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });
        colaboradorRepository.delete(colaborador);
    }
}
