package com.diagnostico_service.service;

import com.diagnostico_service.dto.UsuarioResponseDTO;
import com.diagnostico_service.entities.Empresa;
import com.diagnostico_service.entities.Usuario;
import com.diagnostico_service.enums.EstadoUsuario;
import com.diagnostico_service.enums.Role;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.maps.IMapUsuario;
import com.diagnostico_service.repository.EmpresaRepository;
import com.diagnostico_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private IMapUsuario mapUsuario;

    @Autowired
    private EmpresaRepository empresaRepository;

    public UsuarioResponseDTO saveUser(Usuario usuario) throws Exception {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()){
            throw new BadUserCredentialsException("Ya existe un usuario con este correo: "+ usuario.getUsername() + ".");
        }
        if (usuario.getEmpresa() == null){
            throw new BadUserCredentialsException("La empresa no puede estar vacia.");
        }
        Optional<Empresa> empresaOptional = empresaRepository.findById(usuario.getEmpresa().getNitEmpresa());
        if (empresaOptional.isEmpty()){
            throw new BadUserCredentialsException("La empresa con el nit " + usuario.getEmpresa().getNitEmpresa() + " no existe.");
        }
        Empresa empresa = empresaOptional.get();
        if (usuario.getAniosVinculado() == null){
            throw new BadUserCredentialsException("Los años vinculados no pueden estar vacios.");
        }
        if (empresaRepository.findUsuarioByNitEmpresa(usuario.getEmpresa().getNitEmpresa()).isPresent()){
            throw new BadUserCredentialsException("Ya existe un usuario para la empresa seleccionada.");
        }
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d/*\\-_.º!?¿'¡#!$%&]{6,}$";
        if (!usuario.getPassword().matches(passwordRegex)){
            throw new BadUserCredentialsException("La contraseña debe tener al menos 6 caracteres y contener al menos una letra y un número.");
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!usuario.getUsername().matches(emailRegex)){
            throw new BadUserCredentialsException("El correo no es valido.");
        }
        usuario.setEmpresa(empresa);
        usuario.setEstadoUsuario(EstadoUsuario.Inactivo);
        usuario.setRole(Role.CLIENTE);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFormTerminado(false);

        return mapUsuario.mapUsuario(usuarioRepository.save(usuario));
    }

    public String generateToken(String username) throws ObjectNotFoundException {
        return jwtService.generateToken(username);
    }

    public Map<String, Object> validateToken(String token){
        return jwtService.validateToken(token);
    }
}
