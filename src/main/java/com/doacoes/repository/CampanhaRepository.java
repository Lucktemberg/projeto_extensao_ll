package com.doacoes.repository;

import com.doacoes.entity.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

    @Query("""
            SELECT DISTINCT c
            FROM Campanha c
            LEFT JOIN FETCH c.itens
            ORDER BY c.id DESC
            """)
    List<Campanha> findAllWithItens();

    @Query("""
            SELECT DISTINCT c
            FROM Campanha c
            LEFT JOIN FETCH c.itens
            WHERE c.id = :id
            """)
    Optional<Campanha> findByIdWithItens(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT c
            FROM Campanha c
            LEFT JOIN FETCH c.itens
            WHERE c.ativa = true
            ORDER BY c.id DESC
            """)
    List<Campanha> findAtivasWithItens();
}
