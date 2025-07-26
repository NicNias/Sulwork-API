package com.app.sulwork.mappers;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.entity.ColaboradorEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {
    ColaboradorEntity toModel(ColaboradorDto colaboradorDto);
    ColaboradorDto toDto(ColaboradorEntity colaboradorEntity);

    List<ColaboradorDto> listColaboradorDto(List<ColaboradorEntity> colaboradores);
}
