package com.doacoes.repository;

import com.doacoes.entity.Receptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptorRepository
        extends JpaRepository<Receptor, Long> {

    @Query("""
            SELECT r
            FROM Receptor r
            WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR r.cpf LIKE CONCAT('%', :termo, '%')
               OR LOWER(r.email) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR r.telefone LIKE CONCAT('%', :termo, '%')
               OR LOWER(r.endereco) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    List<Receptor> pesquisar(
            @Param("termo") String termo
    );

    List<Receptor> findAllByOrderByNomeAsc();

    List<Receptor> findByCpfIsNotNull();
}