package com.doacoes.repository;

import com.doacoes.entity.TamanhoRoupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TamanhoRoupaRepository
        extends JpaRepository<TamanhoRoupa, Long> {

    Optional<TamanhoRoupa>
    findFirstByTamanhoIgnoreCaseAndTipoIgnoreCase(
            String tamanho,
            String tipo
    );

    List<TamanhoRoupa> findAllByOrderByTamanhoAsc();
}