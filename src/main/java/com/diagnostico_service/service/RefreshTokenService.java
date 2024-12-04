package com.diagnostico_service.service;

import com.diagnostico_service.entities.RefreshToken;
import com.diagnostico_service.exceptions.ExpiredRefreshTokenException;
import com.diagnostico_service.repository.RefreshTokenRepository;
import com.diagnostico_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .usuario(usuarioRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpire))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new ExpiredRefreshTokenException("El token de actualización " + token.getToken() +  " esta vencido. Inicia sesión de nuevo");
        }
        return token;
    }



    public Optional<RefreshToken> findByUsername(String username){
        return refreshTokenRepository.findByUsername(username);
    }

    public void DeleteRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.delete(refreshToken);
    }

}
