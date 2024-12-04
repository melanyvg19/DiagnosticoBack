package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Query("SELECT c.nombreCategoria FROM Categoria c WHERE c.idCategoria = :idCategoria")
    String getNombreCategoriaByIdCategoria(Integer idCategoria);

    List<Categoria> findByIdCategoriaIn(List<Integer> idCategoria);

}

