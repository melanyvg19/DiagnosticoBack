package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Glosario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlosarioRepository extends JpaRepository<Glosario, Integer> {
}
