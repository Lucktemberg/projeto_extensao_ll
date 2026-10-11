package com.doacoes.repository;

import com.doacoes.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLoginIgnoreCase(
            String login
    );

    boolean existsByLoginIgnoreCase(
            String login
    );

    boolean existsByLoginIgnoreCaseAndIdNot(
            String login,
            Long id
    );

    List<Usuario> findAllByOrderByNomeAsc();
}