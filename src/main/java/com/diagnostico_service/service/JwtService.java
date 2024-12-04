package com.diagnostico_service.service;

import com.diagnostico_service.entities.Usuario;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Map<String,Object> validateToken(final String token){
        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            response.put("isValid", true);
            return response;
        } catch (JwtException e) {
            response.put("isValid", false);
            return response;
        }
    }

    public String generateToken(String username) throws ObjectNotFoundException {
        Map<String,Object> claims = new HashMap<>();
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado"));
        claims.put("nitEmpresa",usuario.getEmpresa().getNitEmpresa());
        claims.put("role",usuario.getRole());
        return createToke(claims,username);
    }

    public String decodeToken(String token){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("\\.");
        String payload = new String(decoder.decode(parts[1]));
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map claims = mapper.readValue(payload, Map.class);
            return (String) claims.get("nitEmpresa");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT payload", e);
        }
    }

    private String createToke(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpire))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Usuario getUsuarioFromAuthorizationHeader(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = extractUsername(token);
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        if (usuarioOptional.isEmpty()){
            throw new BadUserCredentialsException("El usuario no existe");
        }
        return usuarioOptional.get();
    }
}
