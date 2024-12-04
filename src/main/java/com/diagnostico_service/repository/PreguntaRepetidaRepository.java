package com.diagnostico_service.repository;

import com.diagnostico_service.entities.PreguntaRepetida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreguntaRepetidaRepository extends JpaRepository<PreguntaRepetida, Integer> {

    boolean existsPreguntaRepetidaByPreguntaIdPregunta(Integer id);

    @Query("SELECT p.categoria.idCategoria FROM PreguntaRepetida p WHERE p.pregunta.idPregunta = :id")
    Integer findIdCategoriaByPreguntaIdPregunta(Integer id);

    @Query("SELECT DISTINCT p.categoria.idCategoria FROM PreguntaRepetida p")
    List<Integer> findIdCategoria();

}
