package com.app.sulwork.repository;

import com.app.sulwork.entity.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, UUID> {
    Optional<ColaboradorEntity> findByNome(String nome);
    Optional<ColaboradorEntity> findByCpf(String cpf);
}
