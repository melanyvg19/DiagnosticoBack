package com.diagnostico_service.controller;

import com.diagnostico_service.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping
    public ResponseEntity<?> getUsuarioById(){
        return ResponseEntity.ok("despliegue dx 2");
    }

    @GetMapping("/single")
    public ResponseEntity<?> getUsuarioByAuthHeader(@RequestHeader HttpHeaders headers, @RequestParam(name = "idUsuario", required = false) Integer idUsuario){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.getUsuario(Objects.requireNonNull(headers.getFirst("Authorization")),idUsuario));
    }

    @GetMapping("/formulario-finished")
    public ResponseEntity<?> isFormularioFinished(@RequestHeader HttpHeaders headers){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.isFormularioFinished(Objects.requireNonNull(headers.getFirst("Authorization"))));
    }

    @PostMapping("/send-pdf")
    public Map<String, String> sendReport(@RequestParam("graficaImage") MultipartFile graficaImage,
                           @RequestParam("termometroImage") MultipartFile termometroImage,
                           @RequestHeader HttpHeaders headers, @RequestParam(name = "idUsuario", required = false) Integer idUsuario) {
        Map<String, String> response = new HashMap<>();
        try {
            InputStream graficaImageStream = graficaImage.getInputStream();
            InputStream termometroImageStream = termometroImage.getInputStream();
            usuarioService.sendFinishedFormEmail(graficaImageStream, termometroImageStream, Objects.requireNonNull(headers.getFirst("Authorization")), idUsuario);
            response.put("respuesta", "Correo enviado exitosamente");
            return response;
        } catch (IOException e) {
            response.put("try", e.getMessage());
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/download-pdf")
    public ResponseEntity<?> downloadPdf(
            @RequestParam("graficaImage") MultipartFile graficaImage,
            @RequestParam("termometroImage") MultipartFile termometroImage,
            @RequestHeader HttpHeaders headers) {
        try {
            InputStream graficaImageStream = graficaImage.getInputStream();
            InputStream termometroImageStream = termometroImage.getInputStream();
            Map<String, Object> response = usuarioService.downloadPdf(graficaImageStream, termometroImageStream, Objects.requireNonNull(headers.getFirst("Authorization")));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Manejar errores y devolver un mensaje apropiado
            return new ResponseEntity<>(Map.of("error", "Error al generar el PDF: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
