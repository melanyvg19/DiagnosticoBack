package com.diagnostico_service.controller;

import com.diagnostico_service.dto.FormularioRequestDTO;
import com.diagnostico_service.service.FormularioServiceImpl;
import com.diagnostico_service.service.PuntuacionTotalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/formulario")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class FormularioController {

    @Autowired
    private PuntuacionTotalServiceImpl puntuacionTotalService;

    @Autowired
    private FormularioServiceImpl formularioService;

    @PostMapping("/{nitEmpresa}/{fecha}")
    public ResponseEntity<?> addPuntuacionTotal(@PathVariable(name = "nitEmpresa") String nitEmpresa, @PathVariable(name = "fecha") Date fecha){
        return ResponseEntity.ok(puntuacionTotalService.addPuntuacionTotal(nitEmpresa,fecha));
    }

    @PostMapping
    public ResponseEntity<?> crearObjeto(@RequestBody List<FormularioRequestDTO> formularioRequestDTOList, @RequestHeader HttpHeaders headers) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(formularioService.crearObjeto(formularioRequestDTOList, Objects.requireNonNull(headers.getFirst("Authorization"))));
    }

}
