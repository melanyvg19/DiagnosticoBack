package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Opcion;
import com.diagnostico_service.entities.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta,Integer> {

    @Query("SELECT o FROM Pregunta p INNER JOIN Opcion o ON p.idPregunta = o.pregunta.idPregunta " +
            "WHERE p.categoria.idCategoria = :idCategoria")
    List<Opcion> findOpcionesByCategoria(Long idCategoria);

    @Query("SELECT p FROM Pregunta p WHERE p.categoria.idCategoria = :idCategoria")
    List<Pregunta> findByCategoria(Long idCategoria);

    List<Pregunta> findByIdPreguntaIn(List<Integer> preguntaList);

    @Query("SELECT COUNT(*) FROM Pregunta p WHERE p.categoria.idCategoria = :idCategoria")
    Integer findCantidadPreguntaByCategoria(Integer idCategoria);

    @Query("SELECT p.nombrePregunta FROM Pregunta p WHERE p.categoria.idCategoria = :idCategoria ORDER BY p.idPregunta")
    List<String> findNombrePregunta(Integer idCategoria);

    @Query("SELECT COUNT(*) FROM Pregunta p LEFT JOIN PreguntaRepetida r ON p.idPregunta = r.pregunta.idPregunta WHERE r.pregunta.idPregunta IS NULL")
    Integer countAllPregunta();

}