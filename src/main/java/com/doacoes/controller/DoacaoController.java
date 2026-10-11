package com.doacoes.controller;

import com.doacoes.dto.AcompanhamentoDoacaoDTO;
import com.doacoes.dto.DestinoEntregaDTO;
import com.doacoes.dto.DistribuirDoacaoRequestDTO;
import com.doacoes.dto.DoacaoRequestDTO;
import com.doacoes.dto.DoacaoResponseDTO;
import com.doacoes.dto.DoacaoStatusRequestDTO;
import com.doacoes.entity.Doacao;
import com.doacoes.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(
            DoacaoService doacaoService) {

        this.doacaoService = doacaoService;
    }

    // CREATE

    @PostMapping
    public ResponseEntity<DoacaoResponseDTO> salvar(
            @Valid
            @RequestBody DoacaoRequestDTO dto) {

        Doacao doacao =
                doacaoService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doacaoService.toResponse(doacao));
    }

    // READ - LISTAR

    @GetMapping
    public ResponseEntity<List<DoacaoResponseDTO>> listar() {
        return ResponseEntity.ok(doacaoService.listarRespostas());
    }

    @GetMapping("/destinos")
    public ResponseEntity<List<DestinoEntregaDTO>> destinos() {
        return ResponseEntity.ok(doacaoService.listarDestinos());
    }

    @PostMapping("/{id:\\d+}/distribuir")
    public ResponseEntity<DoacaoResponseDTO> distribuir(
            @PathVariable Long id,
            @Valid @RequestBody DistribuirDoacaoRequestDTO dto) {

        Doacao doacao = doacaoService.distribuir(id, dto);
        return ResponseEntity.ok(doacaoService.toResponse(doacao));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<AcompanhamentoDoacaoDTO> acompanhar(
            @PathVariable String codigo) {

        return ResponseEntity.ok(doacaoService.acompanhar(codigo));
    }

    // READ - BUSCAR ID

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<DoacaoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(doacaoService.buscarResposta(id));
    }

    // UPDATE

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<DoacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody DoacaoRequestDTO dto) {

        Doacao doacao = doacaoService.atualizar(id, dto);
        return ResponseEntity.ok(doacaoService.toResponse(doacao));
    }

    @PatchMapping("/{id:\\d+}/status")
    public ResponseEntity<DoacaoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody DoacaoStatusRequestDTO dto) {

        Doacao doacao = doacaoService.atualizarStatus(id, dto);
        return ResponseEntity.ok(doacaoService.toResponse(doacao));
    }

    // DELETE

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        doacaoService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // PESQUISAR

    @GetMapping("/pesquisar")
    public ResponseEntity<List<DoacaoResponseDTO>> pesquisar(
            @RequestParam(required = false)
            String termo) {

        return ResponseEntity.ok(doacaoService.pesquisarRespostas(termo));
    }
}