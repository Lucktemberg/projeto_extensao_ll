package com.doacoes.controller;

import com.doacoes.config.AutoridadesUsuario;
import com.doacoes.dto.LoginRequestDTO;
import com.doacoes.dto.UsuarioRequestDTO;
import com.doacoes.dto.UsuarioResponseDTO;
import com.doacoes.entity.Usuario;
import com.doacoes.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityContextRepository securityContextRepository;

    public UsuarioController(
            UsuarioService usuarioService,
            SecurityContextRepository securityContextRepository) {

        this.usuarioService = usuarioService;
        this.securityContextRepository = securityContextRepository;
    }

    // =====================================================
    // AUTENTICAR
    // =====================================================

    @PostMapping("/autenticar")
    public ResponseEntity<UsuarioResponseDTO> autenticar(
            @Valid
            @RequestBody LoginRequestDTO dto,
            HttpServletRequest request,
            HttpServletResponse response) {

        Usuario usuario = usuarioService.autenticar(dto);

        UsernamePasswordAuthenticationToken autenticacao =
                new UsernamePasswordAuthenticationToken(
                        usuario.getLogin(),
                        null,
                        AutoridadesUsuario.de(usuario)
                );

        SecurityContext contexto = SecurityContextHolder.createEmptyContext();
        contexto.setAuthentication(autenticacao);
        SecurityContextHolder.setContext(contexto);
        securityContextRepository.saveContext(contexto, request, response);

        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> usuarioAtual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioService.buscarPorLogin(authentication.getName());
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(
            @Valid
            @RequestBody UsuarioRequestDTO dto) {

        Usuario usuario =
                usuarioService.salvar(
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new UsuarioResponseDTO(
                                usuario
                        )
                );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {

        List<UsuarioResponseDTO> usuarios =
                usuarioService
                        .listar()
                        .stream()
                        .map(
                                UsuarioResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                usuarios
        );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Usuario usuario =
                usuarioService
                        .buscarPorId(id);

        return ResponseEntity.ok(
                new UsuarioResponseDTO(
                        usuario
                )
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody UsuarioRequestDTO dto) {

        Usuario usuario =
                usuarioService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new UsuarioResponseDTO(
                        usuario
                )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        usuarioService.deletar(
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}