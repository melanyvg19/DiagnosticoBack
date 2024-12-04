package com.diagnostico_service.controller;

import com.diagnostico_service.entities.Empresa;
import com.diagnostico_service.service.EmpresaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class EmpresaController {

    @Autowired
    private EmpresaServiceImpl empresaService;

    @PostMapping
    public ResponseEntity<?> addEmpresa(@RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.saveEmpresa(empresa));
    }
}
