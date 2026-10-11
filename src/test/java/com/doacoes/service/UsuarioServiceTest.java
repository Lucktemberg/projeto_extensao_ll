package com.doacoes.service;

import com.doacoes.dto.UsuarioRequestDTO;
import com.doacoes.entity.Usuario;
import com.doacoes.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private final UsuarioRepository usuarioRepository =
            mock(UsuarioRepository.class);
    private final PasswordEncoder passwordEncoder =
            mock(PasswordEncoder.class);
    private final UsuarioService usuarioService =
            new UsuarioService(usuarioRepository, passwordEncoder);

    @Test
    void deveManterSenhaAtualQuandoEdicaoNaoInformaNovaSenha() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNome("USUÁRIO");
        existente.setLogin("usuario");
        existente.setSenha("hash-atual");
        existente.setPerfil("USUARIO");

        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome("Usuário atualizado");
        dto.setLogin("usuario");
        dto.setSenha("");
        dto.setPerfil("USUARIO");
        dto.setPermissoes(List.of("ESTOQUE"));

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario atualizado = usuarioService.atualizar(1L, dto);

        assertEquals("hash-atual", atualizado.getSenha());
        assertEquals("USUÁRIO ATUALIZADO", atualizado.getNome());
        verify(passwordEncoder, never()).encode(anyString());
    }
}
