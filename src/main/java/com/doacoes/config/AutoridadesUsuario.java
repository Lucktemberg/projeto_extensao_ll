package com.doacoes.config;

import com.doacoes.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class AutoridadesUsuario {

    private AutoridadesUsuario() {
    }

    public static Collection<GrantedAuthority> de(Usuario usuario) {
        List<GrantedAuthority> autoridades = new ArrayList<>();

        if (usuario.getPerfil() != null
                && "ADMIN".equalsIgnoreCase(usuario.getPerfil().trim())) {
            autoridades.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (usuario.getPermissoes() != null) {
            usuario.getPermissoes().stream()
                    .filter(permissao -> permissao != null && !permissao.isBlank())
                    .map(permissao -> permissao.trim().toUpperCase())
                    .distinct()
                    .map(SimpleGrantedAuthority::new)
                    .forEach(autoridades::add);
        }

        return autoridades;
    }
}
