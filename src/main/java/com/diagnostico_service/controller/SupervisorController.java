package com.diagnostico_service.controller;

import com.diagnostico_service.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/supervisor")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class SupervisorController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping("/clients")
    public ResponseEntity<?> findUsersOfSupervisor(@RequestHeader HttpHeaders headers, @RequestParam(name = "idSupervisor", required = false) Integer idSupervisor) {
        return ResponseEntity.ok(usuarioService.findClientsOfSupervisor(Objects.requireNonNull(headers.getFirst("Authorization")), idSupervisor));
    }
}
