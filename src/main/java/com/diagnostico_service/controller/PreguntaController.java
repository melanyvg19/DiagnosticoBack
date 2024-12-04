package com.diagnostico_service.controller;


import com.diagnostico_service.service.PreguntaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pregunta")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class PreguntaController {

    @Autowired
    private PreguntaServiceImpl preguntaService;

    @GetMapping("/opciones_categoria/{categoria}")
    public ResponseEntity<?> getOpcionesByCategoria(@PathVariable(name = "categoria") Long idCategoria) throws Exception {
        return ResponseEntity.ok(preguntaService.getOpcionesByCategoria(idCategoria));
    }

    @GetMapping("/preguntas_by_categoria/{categoria}")
    public ResponseEntity<?> getPreguntaByCategoria(@PathVariable(name = "categoria") Long idCategoria) throws Exception {
        return ResponseEntity.ok(preguntaService.getPreguntaByCategoria(idCategoria));
    }

    @GetMapping("/preguntas_respuestas_by_categoria/{categoria}")
    public ResponseEntity<?> getPreguntaRespuestasByCategoria(@PathVariable(name = "categoria") Long idCategoria) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(preguntaService.getPreguntaRespuestas(idCategoria));
    }

}
