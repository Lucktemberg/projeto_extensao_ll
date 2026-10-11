package com.doacoes.controller;

import com.doacoes.dto.CampanhaRequestDTO;
import com.doacoes.dto.CampanhaResponseDTO;
import com.doacoes.service.CampanhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campanhas")
public class CampanhaController {

    private final CampanhaService campanhaService;

    public CampanhaController(CampanhaService campanhaService) {
        this.campanhaService = campanhaService;
    }

    @PostMapping
    public ResponseEntity<CampanhaResponseDTO> salvar(
            @Valid @RequestBody CampanhaRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campanhaService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CampanhaResponseDTO>> listar(
            @RequestParam(defaultValue = "false") boolean ativas) {

        return ResponseEntity.ok(campanhaService.listar(ativas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(campanhaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampanhaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CampanhaRequestDTO dto) {

        return ResponseEntity.ok(campanhaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        campanhaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
