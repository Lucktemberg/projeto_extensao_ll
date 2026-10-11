package com.doacoes.repository;

import com.doacoes.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository
        extends JpaRepository<Estoque, Long> {

    Optional<Estoque>
    findFirstByDescricaoIgnoreCaseAndCategoriaIgnoreCase(
            String descricao,
            String categoria
    );

    long countByQuantidadeLessThanEqual(Integer quantidade);

    List<Estoque> findByQuantidadeLessThanEqualOrderByDescricaoAsc(Integer quantidade);

    long countByQuantidadeGreaterThanAndQuantidadeLessThanEqual(
            Integer minimo,
            Integer maximo
    );

    @Query("SELECT COUNT(e) FROM Estoque e WHERE COALESCE(e.quantidade, 0) <= 0")
    long countItensSemEstoque();

    @Query("SELECT COALESCE(SUM(e.quantidade), 0) FROM Estoque e")
    Long somarQuantidadeTotal();

    List<Estoque> findAllByOrderByDescricaoAsc();
}