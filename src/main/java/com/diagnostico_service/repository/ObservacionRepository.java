package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ObservacionRepository extends JpaRepository<Observacion, Integer> {

    @Query("SELECT o.textoObservacion FROM Observacion o WHERE o.empresa.nitEmpresa = :nitEmpresa AND o.categoria.idCategoria = :idCategoria AND o.fecha = :fecha")
    String obtenerTextoObservacion(String nitEmpresa, Integer idCategoria, Date fecha);
}
