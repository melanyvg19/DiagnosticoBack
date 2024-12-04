package com.diagnostico_service.repository;

import com.diagnostico_service.entities.PuntuacionTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PuntuacionTotalRepository extends JpaRepository<PuntuacionTotal, Long> {

    @Query("SELECT p FROM PuntuacionTotal p WHERE p.categoria.idCategoria = :idCategoria AND p.empresa.nitEmpresa = :nitEmpresa ORDER BY p.fecha DESC LIMIT 1")
    Optional<PuntuacionTotal> getPuntuacionTotalByIdCategoria(Integer idCategoria, String nitEmpresa);

    @Query("SELECT DISTINCT pt.categoria.idCategoria FROM PuntuacionTotal pt WHERE pt.empresa.nitEmpresa = :nitEmpresa AND pt.categoria.idCategoria NOT IN (SELECT pr.categoria.idCategoria FROM PreguntaRepetida pr) ORDER BY pt.categoria.idCategoria")
    List<Integer> getIdCategoriasByNitEmpresaNotInPreguntaRepetida(String nitEmpresa);

    @Query("SELECT AVG(p.puntuacionTotal) FROM PuntuacionTotal p WHERE p.empresa.nitEmpresa = :nitEmpresa AND p.fecha = :fecha")
    Double findAverageTotalByNitEmpresaAndFecha(String nitEmpresa, Date fecha);


}
