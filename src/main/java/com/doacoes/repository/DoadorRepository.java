package com.doacoes.repository;

import com.doacoes.entity.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Long> {

    @Query("""
            SELECT d
            FROM Doador d
            WHERE LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR d.cpf LIKE CONCAT('%', :termo, '%')
               OR LOWER(d.email) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR d.telefone LIKE CONCAT('%', :termo, '%')
               OR LOWER(d.endereco) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    List<Doador> pesquisar(@Param("termo") String termo);

    List<Doador> findAllByOrderByNomeAsc();

    List<Doador> findByCpfIsNotNull();
}