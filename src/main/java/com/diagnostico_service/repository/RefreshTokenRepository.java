package com.diagnostico_service.repository;

import com.diagnostico_service.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Query("SELECT r FROM RefreshToken r INNER JOIN Usuario u ON r.usuario.idUsuario = u.idUsuario WHERE u.username = :username")
    Optional<RefreshToken> findByUsername(String username);

}
