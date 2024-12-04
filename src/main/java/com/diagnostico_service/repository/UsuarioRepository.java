package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Usuario;
import com.diagnostico_service.enums.EstadoUsuario;
import com.diagnostico_service.enums.Role;
import com.diagnostico_service.projection.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u.role FROM Usuario u WHERE u.idUsuario = :idUsuario")
    Role findRoleByIdUsuario(Integer idUsuario);

    @Query("SELECT u.estadoUsuario FROM Usuario u WHERE u.username = :nombre")
    EstadoUsuario findEstadoUsuarioByUsername(String nombre);

    @Query("SELECT u.role FROM Usuario u WHERE u.username = :nombre")
    Role findRoleByUsername(String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.supervisor IS NULL AND u.role = 'CLIENTE'")
    List<Usuario> findAllUsersWithoutSupervisor();

    @Query(value = "SELECT DISTINCT p.id_pregunta,f.id_categoria ,p.nombre_pregunta AS nombrePregunta, " +
            " GROUP_CONCAT(o.texto_opcion ORDER BY o.texto_opcion ASC SEPARATOR '; ') AS respuesta" +
            " FROM pregunta p INNER JOIN formulario f ON p.id_pregunta = f.id_pregunta\n" +
            " INNER JOIN opcion o ON f.id_opcion = o.id_opcion " +
            " WHERE nit_empresa = :nitEmpresa AND f.fecha_aplicacion = :date AND f.id_categoria = :idCategoria " +
            " GROUP BY p.id_pregunta, f.id_categoria, p.nombre_pregunta ORDER BY f.id_categoria,p.id_pregunta", nativeQuery = true)
    List<ReportDTO> reportData(String nitEmpresa, Date date, Integer idCategoria);

    @Query("SELECT u FROM Usuario u WHERE u.supervisor.idUsuario = :idUsuario")
    List<Usuario> findAllClientsOfSupervisor(Integer idUsuario);

}