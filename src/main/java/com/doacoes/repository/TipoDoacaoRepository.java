package com.doacoes.repository;

import com.doacoes.entity.TipoDoacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoDoacaoRepository
        extends JpaRepository<TipoDoacao, Long> {

    boolean existsByNomeIgnoreCaseAndCategoriaIgnoreCase(
            String nome,
            String categoria
    );

    boolean existsByNomeIgnoreCaseAndCategoriaIgnoreCaseAndIdNot(
            String nome,
            String categoria,
            Long id
    );

    List<TipoDoacao> findAllByOrderByNomeAsc();
}