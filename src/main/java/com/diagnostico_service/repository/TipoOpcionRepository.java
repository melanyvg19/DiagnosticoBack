package com.diagnostico_service.repository;

import com.diagnostico_service.entities.TipoOpcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoOpcionRepository extends JpaRepository<TipoOpcion, Integer> {
}
