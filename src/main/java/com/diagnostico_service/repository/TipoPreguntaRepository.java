package com.diagnostico_service.repository;

import com.diagnostico_service.entities.TipoPregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPreguntaRepository extends JpaRepository<TipoPregunta, Integer> {
}
