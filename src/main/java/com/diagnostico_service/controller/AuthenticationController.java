package com.diagnostico_service.controller;

import com.diagnostico_service.dto.AuthRequestDTO;
import com.diagnostico_service.dto.AuthResponseDTO;
import com.diagnostico_service.dto.UsuarioResponseDTO;
import com.diagnostico_service.entities.RefreshToken;
import com.diagnostico_service.entities.Usuario;
import com.diagnostico_service.enums.EstadoUsuario;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ExpiredRefreshTokenException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.repository.UsuarioRepository;
import com.diagnostico_service.service.AuthenticationService;
import com.diagnostico_service.service.JwtService;
import com.diagnostico_service.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public UsuarioResponseDTO addNewUser(@RequestBody Usuario usuario) throws Exception {
        return authenticationService.saveUser(usuario);
    }

    @PostMapping("/login")
    public AuthResponseDTO getToken(@RequestBody AuthRequestDTO authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            EstadoUsuario estadoUsuario = usuarioRepository.findEstadoUsuarioByUsername(authRequest.getUsername());
            if(estadoUsuario == EstadoUsuario.Inactivo){
                throw new BadUserCredentialsException("Su cuenta se encuentra inactiva. Por favor, espere a que sea activada para acceder al aplicativo.");
            }
            Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByUsername(authRequest.getUsername());
            refreshTokenOptional.ifPresent(refreshToken -> refreshTokenService.DeleteRefreshToken(refreshToken));
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            return AuthResponseDTO
                    .builder()
                    .accessToken(authenticationService.generateToken(authRequest.getUsername()))
                    .refreshToken(refreshToken.getToken())
                    .role(usuarioRepository.findRoleByUsername(authRequest.getUsername()))
                    .build();

        }catch (BadUserCredentialsException e){
          throw new BadUserCredentialsException(e.getMessage());
        } catch (Exception e){
            throw new BadUserCredentialsException("Usuario y/o contraseña incorrectas");
        }

    }

    @PostMapping("/refreshToken")
    public AuthResponseDTO refreshToken(@RequestBody AuthResponseDTO authResponseDTO){

        return refreshTokenService.findByToken(authResponseDTO.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsuario)
                .map(userCredential -> {
                    String accessToken = null;
                    try {
                        accessToken = jwtService.generateToken(userCredential.getUsername());
                    } catch (ObjectNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return AuthResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(authResponseDTO.getRefreshToken()).build();
                }).orElseThrow(() ->new ExpiredRefreshTokenException("El refresh token no se encuentra en la base de datos"));
    }

    @GetMapping("/validateToken/{token}")
    public Map<String, Object> validateToken(@PathVariable(name = "token") String token){
        return authenticationService.validateToken(token);
    }
}
