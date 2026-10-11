package com.doacoes.repository;

import com.doacoes.entity.Distribuicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistribuicaoRepository
        extends JpaRepository<Distribuicao, Long> {

    boolean existsByReceptorId(Long receptorId);

    boolean existsByEstoqueId(Long estoqueId);

    List<Distribuicao> findAllByOrderByIdDesc();

    List<Distribuicao> findByDataBetween(
            java.time.LocalDateTime inicio,
            java.time.LocalDateTime fim
    );
}