package com.doacoes.repository;

import com.doacoes.entity.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    @Query("""
            SELECT d
            FROM Doacao d
            WHERE LOWER(d.nomeDoador) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(d.descricaoItem) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(d.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(d.unidade) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    List<Doacao> pesquisar(@Param("termo") String termo);

    boolean existsByDoadorId(Long doadorId);

    List<Doacao> findAllByOrderByIdDesc();

    Optional<Doacao> findByCodigoAcompanhamentoIgnoreCase(String codigoAcompanhamento);

    boolean existsByCodigoAcompanhamento(String codigoAcompanhamento);

    List<Doacao> findByCampanhaId(Long campanhaId);

    boolean existsByCampanhaId(Long campanhaId);

    long countByStatusIn(Collection<String> status);

    List<Doacao> findByStatusAndCategoriaIgnoreCaseOrderByIdAsc(
            String status,
            String categoria
    );

    List<Doacao> findByDataDoacaoBetween(java.time.LocalDate inicio, java.time.LocalDate fim);
}