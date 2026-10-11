package com.doacoes.config;

import com.doacoes.entity.Usuario;
import com.doacoes.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioDetalhesService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetalhesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .authorities(AutoridadesUsuario.de(usuario))
                .build();
    }
}
