package com.app.sulwork.repository;

import com.app.sulwork.entity.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, String> {
    Optional<ColaboradorEntity> findByNome(String nome);
    Optional<ColaboradorEntity> findByCpf(String cpf);
    Optional<ColaboradorEntity> findById(String id);

    @Query(value = """
    SELECT c.*
    FROM colaborador c
    JOIN colaborador_entity_itens ci ON c.id = ci.colaborador_entity_id
    WHERE c.data_cafe = :dataCafe
      AND ci.itens IN (:itens)
""", nativeQuery = true)
    List<ColaboradorEntity> findByDataCafeAndItens(@Param("dataCafe") LocalDate dataCafe, @Param("itens") List<String> itens);
}
