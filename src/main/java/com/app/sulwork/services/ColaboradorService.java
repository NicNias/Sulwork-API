package com.app.sulwork.services;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.entity.ColaboradorEntity;
import com.app.sulwork.exceptions.UserAlreadyExistsException;
import com.app.sulwork.mappers.ColaboradorMapper;
import com.app.sulwork.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColaboradorService {
    private final ColaboradorMapper colaboradorMapper;
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorDto createColaborador(ColaboradorDto colaboradorDto) {
        colaboradorRepository.findByNome(colaboradorDto.nome()).ifPresent(colaboradorEntity -> {
            throw new UserAlreadyExistsException("Colaborador ja cadastrado");
        });

        colaboradorRepository.findByCpf(colaboradorDto.cpf()).ifPresent(colaboradorEntity -> {
            throw new UserAlreadyExistsException("CPF de colaborador ja cadastrado");
        });

        ColaboradorEntity colaboradorNew = colaboradorMapper.toModel(colaboradorDto);

        colaboradorRepository.save(colaboradorNew);

        return colaboradorMapper.toDto(colaboradorNew);
    }
}
