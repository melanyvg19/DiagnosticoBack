package com.diagnostico_service.controller;

import com.diagnostico_service.service.PuntuacionTotalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/puntuacion_total")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class PuntuacionTotalController {

    @Autowired
    private PuntuacionTotalServiceImpl puntuacionTotalService;

    @GetMapping
    public ResponseEntity<?> getPuntuacionTotal(@RequestHeader HttpHeaders headers) throws Exception {

        return ResponseEntity.ok(puntuacionTotalService.getPuntuacionTotalForCharts(headers.getFirst("Authorization")));
    }

    @GetMapping("/extra")
    public ResponseEntity<?> getPuntuacionTotalExtra(@RequestHeader HttpHeaders headers) throws Exception {
        return ResponseEntity.ok(puntuacionTotalService.getPuntuacionTotalForChartsExtra(headers.getFirst("Authorization")));
    }

}
