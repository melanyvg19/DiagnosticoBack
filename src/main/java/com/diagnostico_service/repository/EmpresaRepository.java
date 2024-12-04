package com.diagnostico_service.repository;

import com.diagnostico_service.dto.CompanyDateFormDTO;
import com.diagnostico_service.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa,String> {

    Optional<Empresa> findEmpresaByNitEmpresa(String nitEmpresa);

    @Query("SELECT u FROM Usuario u INNER JOIN Empresa e ON u.empresa.nitEmpresa = e.nitEmpresa WHERE e.nitEmpresa = :nitEmpresa")
    Optional<Empresa> findUsuarioByNitEmpresa(String nitEmpresa);

    @Query("SELECT new com.diagnostico_service.dto.CompanyDateFormDTO(e.nitEmpresa, f.fecha) FROM Empresa e INNER JOIN Formulario f ON e.nitEmpresa = f.empresa.nitEmpresa " +
            "INNER JOIN Usuario u ON u.empresa.nitEmpresa = e.nitEmpresa GROUP BY e.nitEmpresa, f.fecha ORDER BY f.fecha")
    List<CompanyDateFormDTO> findEmpresasFormulario();

}
