package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface FormularioRepository extends JpaRepository<Formulario,Long > {

    @Query("SELECT DISTINCT f.categoria.idCategoria FROM Formulario f WHERE f.empresa.nitEmpresa = :nitEmpresa ORDER BY f.categoria.idCategoria")
    List<Integer> findIdCategoriasByNitEmpresa(String nitEmpresa);

    @Query("SELECT SUM(o.puntuacion) FROM Formulario f INNER JOIN Opcion o ON f.opcion.idOpcion=o.idOpcion WHERE f.categoria.idCategoria = :idCategoria AND f.empresa.nitEmpresa = :nitEmpresa AND f.fecha = :fecha")
    Float findPuntuacionTotalByIdCategoria(Integer idCategoria, String nitEmpresa, Date fecha);

    @Query("SELECT f FROM Formulario f ORDER BY f.idFormulario DESC LIMIT 1")
    Formulario findLastFormulario();

    @Query("SELECT f.fecha FROM Formulario f WHERE f.empresa.nitEmpresa = :nitEmpresa ORDER BY f.fecha LIMIT 1")
    Date findLastFecha(String nitEmpresa);

}
