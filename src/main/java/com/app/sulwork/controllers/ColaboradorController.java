package com.app.sulwork.controllers;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.services.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/colaborador")
public class ColaboradorController {
    private final ColaboradorService colaboradorService;

    @PostMapping("/create")
    public ResponseEntity<ColaboradorDto> saveColaborador(@RequestBody @Valid ColaboradorDto colaboradorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.createColaborador(colaboradorDto));
    }
}
