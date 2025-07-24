package com.app.sulwork.services;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.dto.UpdatedCafeDto;
import com.app.sulwork.entity.ColaboradorEntity;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresAlreadyExistsEception;
import com.app.sulwork.exceptions.colaboradores.ColaboradoresNotFoundException;
import com.app.sulwork.exceptions.colaboradores.ItemsAlreadyRegisteredForDateException;
import com.app.sulwork.mappers.ColaboradorMapper;
import com.app.sulwork.repository.ColaboradorRepository;
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

    public List<ColaboradorDto> findAll() {
        List<ColaboradorEntity> colaboradores = colaboradorRepository.findAll();
        if (colaboradores.isEmpty()) {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        }

        return colaboradorMapper.ListColaboradorDto(colaboradores);
    }

    public ColaboradorDto updateDataCafeAndItens(String id, UpdatedCafeDto updatedCafeDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(updatedCafeDto.dataCafe(), updatedCafeDto.itens());
        boolean temConflito = conflitos.stream().anyMatch(c -> !c.getId().equals(id));

        if (temConflito) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        colaborador.setDataCafe(updatedCafeDto.dataCafe());
        colaborador.setItens(updatedCafeDto.itens());

        colaboradorRepository.save(colaborador);

        return colaboradorMapper.toDto(colaborador);
    }

    public ColaboradorDto addItensAndUpdateDataCafe(String id, UpdatedCafeDto updatedCafeDto) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });

        Set<String> itensAtuais = new HashSet<>(colaborador.getItens());
        itensAtuais.addAll(updatedCafeDto.itens());

        List<ColaboradorEntity> conflitos = colaboradorRepository.findByDataCafeAndItens(updatedCafeDto.dataCafe(), new ArrayList<>(itensAtuais));
        boolean temConflito = conflitos.stream().anyMatch(c -> !c.getId().equals(id));

        if (temConflito) {
            throw new ItemsAlreadyRegisteredForDateException("Um ou mais itens já foram cadastrados para esta data.");
        }

        colaborador.setDataCafe(updatedCafeDto.dataCafe());
        colaborador.setItens(new ArrayList<>(itensAtuais));

        colaboradorRepository.save(colaborador);

        return colaboradorMapper.toDto(colaborador);
    }


    public void deleteColaborador(String id) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(id).orElseThrow(() -> {
            throw new ColaboradoresNotFoundException("Nenhum Colaborador foi encontrado!");
        });
        colaboradorRepository.delete(colaborador);
    }
}
