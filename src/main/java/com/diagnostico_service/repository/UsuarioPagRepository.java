package com.diagnostico_service.repository;

import com.diagnostico_service.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface UsuarioPagRepository extends ListPagingAndSortingRepository<Usuario, Integer> {

    Page<Usuario> findByNombreCompletoContainingOrUsernameContaining(String nombreCompleto, String username, Pageable pageable);
}
