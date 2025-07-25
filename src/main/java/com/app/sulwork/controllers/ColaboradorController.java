package com.app.sulwork.controllers;

import com.app.sulwork.dto.ColaboradorDto;
import com.app.sulwork.dto.UpdatedCafeDto;
import com.app.sulwork.dto.UpdatedStatusCafeDto;
import com.app.sulwork.services.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/colaborador")
public class ColaboradorController {
    private final ColaboradorService colaboradorService;

    @PostMapping("/create")
    public ResponseEntity<ColaboradorDto> saveColaborador(@Valid @RequestBody ColaboradorDto colaboradorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.createColaborador(colaboradorDto));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<ColaboradorDto>> getAllColaboradores() {
        return ResponseEntity.status(HttpStatus.OK).body(colaboradorService.findAll());
    }

    @PatchMapping("/colaborador/{id}")
    public ResponseEntity<ColaboradorDto> updateColaborador(@PathVariable String id, @RequestBody @Valid ColaboradorDto colaboradorDto) {
        ColaboradorDto updated = colaboradorService.updateColaborador(id, colaboradorDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/cafe/adicionar-itens/{id}")
    public ResponseEntity<ColaboradorDto> addItensAndUpdateDate(@PathVariable String id, @RequestBody @Valid UpdatedCafeDto updatedCafeDto) {
        ColaboradorDto colaboradorUpdated = colaboradorService.addItensAndUpdateDataCafe(id, updatedCafeDto);
        return ResponseEntity.ok(colaboradorUpdated);
    }

    @PatchMapping("/cafe/status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable String id, @RequestBody @Valid UpdatedStatusCafeDto updatedStatusCafeDto) {
        String mensagem = colaboradorService.UpdatedStatus(id, updatedStatusCafeDto);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteColaborador(@PathVariable String id) {
        colaboradorService.deleteColaborador(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
