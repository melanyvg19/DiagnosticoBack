package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionRepository extends JpaRepository <Opcion, Integer> {

    @Query("SELECT o FROM Pregunta p INNER JOIN Opcion o ON p.idPregunta = o.pregunta.idPregunta " +
            "WHERE p.categoria.idCategoria = :idCategoria")
    List<Opcion> findOpcionesByCategoria(Long idCategoria);

}
