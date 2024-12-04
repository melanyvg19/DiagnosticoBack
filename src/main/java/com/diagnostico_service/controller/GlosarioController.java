package com.diagnostico_service.controller;

import com.diagnostico_service.service.GlosarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/glosario")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class GlosarioController {

    @Autowired
    private GlosarioServiceImpl glosarioService;

    @GetMapping("")
    public ResponseEntity<?> allGlosario(){
        return ResponseEntity.ok(glosarioService.allGlosarios());
    }

}
