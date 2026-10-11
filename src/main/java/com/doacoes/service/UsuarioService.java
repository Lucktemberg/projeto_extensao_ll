package com.doacoes.service;

import com.doacoes.dto.LoginRequestDTO;
import com.doacoes.dto.UsuarioRequestDTO;
import com.doacoes.entity.Usuario;
import com.doacoes.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository =
                usuarioRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    // =====================================================
    // AUTENTICAR
    // =====================================================

    @Transactional
    public Usuario autenticar(LoginRequestDTO dto) {

        String login = dto.getLogin().trim();
        String senha = dto.getSenha().trim();

        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCase(login)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuário ou senha inválidos"
                        )
                );

        boolean senhaCorreta = false;

        if (usuario.getSenha() != null) {
            senhaCorreta = passwordEncoder.matches(
                    senha,
                    usuario.getSenha()
            );
        }

        /*
         * Compatibilidade com usuários antigos,
         * onde a senha estava salva em texto puro.
         */
        if (!senhaCorreta
                && usuario.getSenha() != null
                && senha.equals(usuario.getSenha())) {

            usuario.setSenha(
                    passwordEncoder.encode(senha)
            );

            usuarioRepository.save(usuario);

            senhaCorreta = true;
        }

        if (!senhaCorreta) {
            throw new IllegalArgumentException(
                    "Usuário ou senha inválidos"
            );
        }

        return usuario;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public Usuario salvar(
            UsuarioRequestDTO dto) {

        validarSenhaObrigatoria(dto.getSenha());

        validarPerfil(
                dto.getPerfil()
        );

        String login =
                dto.getLogin().trim();

        /*
         * Login não pode ser repetido.
         */
        if (usuarioRepository
                .existsByLoginIgnoreCase(login)) {

            throw new IllegalArgumentException(
                    "Já existe um usuário com esse login"
            );
        }

        Usuario usuario =
                new Usuario();

        preencherUsuario(
                usuario,
                dto
        );

        return usuarioRepository.save(
                usuario
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    public List<Usuario> listar() {

        return usuarioRepository.findAllByOrderByNomeAsc();
    }

    public Usuario buscarPorLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        return usuarioRepository
                .findByLoginIgnoreCase(login.trim())
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado")
                );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public Usuario buscarPorId(
            Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do usuário é obrigatório"
            );
        }

        return usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public Usuario atualizar(
            Long id,
            UsuarioRequestDTO dto) {

        validarPerfil(
                dto.getPerfil()
        );

        Usuario usuario =
                buscarPorId(id);

        String login =
                dto.getLogin().trim();

        /*
         * Verifica se outro usuário
         * já está usando esse login.
         */
        if (usuarioRepository
                .existsByLoginIgnoreCaseAndIdNot(
                        login,
                        id
                )) {

            throw new IllegalArgumentException(
                    "Já existe outro usuário com esse login"
            );
        }

        preencherUsuario(
                usuario,
                dto
        );

        return usuarioRepository.save(
                usuario
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        Usuario usuario =
                buscarPorId(id);

        usuarioRepository.delete(
                usuario
        );
    }

    // =====================================================
    // PREENCHER / FORMATAR USUÁRIO
    // =====================================================

    private void preencherUsuario(
            Usuario usuario,
            UsuarioRequestDTO dto) {

        /*
         * Nome em maiúsculo.
         */
        usuario.setNome(
                dto.getNome()
                        .trim()
                        .toUpperCase()
        );

        /*
         * Login.
         */
        usuario.setLogin(
                dto.getLogin()
                        .trim()
        );

        if (usuario.getId() == null
                || (dto.getSenha() != null
                && !dto.getSenha().isBlank())) {

            usuario.setSenha(
                    passwordEncoder.encode(
                            dto.getSenha().trim()
                    )
            );
        }

        /*
         * Perfil em maiúsculo.
         */
        String perfil =
                dto.getPerfil()
                        .trim()
                        .toUpperCase();

        usuario.setPerfil(
                perfil
        );

        /*
         * Prepara as permissões.
         */
        List<String> permissoes =
                prepararPermissoes(
                        dto.getPermissoes()
                );

        /*
         * ADMIN não precisa possuir
         * permissões individuais.
         */
        if ("ADMIN".equals(perfil)) {

            permissoes.clear();
        }

        usuario.setPermissoes(
                permissoes
        );
    }

    // =====================================================
    // PREPARAR PERMISSÕES
    // =====================================================

    private List<String> prepararPermissoes(
            List<String> permissoes) {

        if (permissoes == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                permissoes
                        .stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(permissao ->
                                !permissao.isBlank()
                        )
                        .map(String::toUpperCase)
                        .distinct()
                        .toList()
        );
    }

    // =====================================================
    // VALIDAR PERFIL
    // =====================================================

    private void validarPerfil(
            String perfil) {

        if (perfil == null
                || perfil.isBlank()) {

            throw new IllegalArgumentException(
                    "Perfil é obrigatório"
            );
        }

        String perfilFormatado =
                perfil
                        .trim()
                        .toUpperCase();

        if (!"ADMIN".equals(perfilFormatado)
                && !"USUARIO".equals(
                perfilFormatado
        )) {

            throw new IllegalArgumentException(
                    "Perfil inválido. Use ADMIN ou USUARIO"
            );
        }
    }

    private void validarSenhaObrigatoria(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException(
                    "Senha é obrigatória"
            );
        }
    }
}