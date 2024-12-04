package com.diagnostico_service.controller;

import com.diagnostico_service.dto.*;
import com.diagnostico_service.entities.Glosario;
import com.diagnostico_service.entities.Opcion;
import com.diagnostico_service.entities.Pregunta;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class AdminController {

    @Autowired
    private UsuarioServiceImpl usuarioServiceImpl;

    @Autowired
    private PreguntaServiceImpl preguntaService;

    @Autowired
    private OpcionServiceImpl opcionService;

    @Autowired
    private GlosarioServiceImpl glosarioService;

    @Autowired
    private PuntuacionTotalServiceImpl puntuacionTotalServiceImpl;

    @GetMapping("/usuario/all")
    public ResponseEntity<?> getAllUsuarios(@RequestParam(name = "page", defaultValue = "0")Integer page,
                                            @RequestParam(name = "elements",defaultValue = "8")Integer elements,
                                            @RequestParam(defaultValue = "idUsuario")String sortBy,
                                            @RequestParam(defaultValue = "DESC")String sortDirection){
        return ResponseEntity.ok(usuarioServiceImpl.getAllUsuarios(page,elements, sortDirection,sortBy));
    }

    @GetMapping("/usuario/search")
    public ResponseEntity<?> searchUsuarios(@RequestParam(name = "query") String query,
                                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "elements", defaultValue = "8") Integer elements,
                                            @RequestParam(defaultValue = "idUsuario")String sortBy,
                                            @RequestParam(defaultValue = "DESC")String sortDirection) {
        return ResponseEntity.ok(usuarioServiceImpl.searchUsuarios(query, page, elements,sortDirection,sortBy));
    }

    @GetMapping("/usuario/no-supervisor")
    public ResponseEntity<?> hasNoSupervisor(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioServiceImpl.findAllUsuariosWithoutSupervisor());
    }

    @PatchMapping("/usuario/update")
    public ResponseEntity<?> UpdateUsuario(@RequestBody UsuarioResponseDTO usuarioResponseDTO) throws ObjectNotFoundException {
        return ResponseEntity.ok(usuarioServiceImpl.updateUsuario(usuarioResponseDTO));
    }

    @PatchMapping("/supervisor/assign-client")
    public ResponseEntity<?> assignSupervisor(@RequestBody AssingSupervisorDTO assingSupervisorDTO) throws ObjectNotFoundException {
        return ResponseEntity.ok(usuarioServiceImpl.assignSupervisor(assingSupervisorDTO));
    }

    @PatchMapping("/supervisor/delete-client")
    public ResponseEntity<?> deleteSupervisorClients(@RequestBody AssingSupervisorDTO assingSupervisorDTO) throws ObjectNotFoundException {
        return ResponseEntity.ok(usuarioServiceImpl.deleteSupervisorClients(assingSupervisorDTO));
    }

    @PostMapping("/pregunta")
    public ResponseEntity<?> addPregunta(@RequestBody Pregunta pregunta) throws Exception {
        return ResponseEntity.ok(preguntaService.addPreguntas(pregunta));
    }

    @DeleteMapping("/pregunta/{id}")
    public ResponseEntity<?> deletePregunta(@PathVariable(name = "id")Integer id) throws Exception {
        return ResponseEntity.ok(preguntaService.deletePregunta(id));
    }

    @PatchMapping("/pregunta")
    public ResponseEntity<?> updatePregunta(@RequestBody PreguntaRequestDTO preguntaRequestDTO) throws Exception {
        return ResponseEntity.ok(preguntaService.updatePregunta(preguntaRequestDTO));
    }

    @PostMapping("/opcion")
    public ResponseEntity<?> addOpcion(@RequestBody Opcion opcion) throws Exception {
        return ResponseEntity.ok(opcionService.addOpcion(opcion));
    }

    @DeleteMapping("/opcion/{id}")
    public ResponseEntity<?> deleteOpcion(@PathVariable(name = "id")Integer id) throws Exception {
        return ResponseEntity.ok(opcionService.deleteOpcion(id));
    }

    @PatchMapping("/opcion")
    public ResponseEntity<?> updateOpcion(@RequestBody OpcionResponseDTO opcionResponseDTO) throws Exception {
        return ResponseEntity.ok(opcionService.updateOpcion(opcionResponseDTO));
    }

    @PostMapping("/glosario")
    public ResponseEntity<?> addGlosario(@RequestBody Glosario glosario){
        return ResponseEntity.ok(glosarioService.addGlosario(glosario));
    }

    @DeleteMapping("/glosario/{id}")
    public ResponseEntity<?> deleteGlosario(@PathVariable(name = "id")Integer id) throws Exception {
        return ResponseEntity.ok(glosarioService.deleteGlosario(id));
    }

    @PatchMapping("/glosario")
    public ResponseEntity<?> updateGlosario(@RequestBody GlosarioDTO glosarioDTO) throws Exception {
        return ResponseEntity.ok(glosarioService.updateGlosario(glosarioDTO));
    }

    @GetMapping("/reports")
    public ResponseEntity<?> companyResults() throws IOException {
        return ResponseEntity.ok(usuarioServiceImpl.csvResults());
    }

    @GetMapping("/reportsData/{nitEmpresa}/{date}/{idCategoria}")
    public ResponseEntity<?> companyResultsData(@PathVariable(name = "nitEmpresa") String nitEmpresa, @PathVariable(name = "date")Date date, @PathVariable(name = "idCategoria")Integer idCategoria){
        return ResponseEntity.ok(usuarioServiceImpl.csvResultsData(nitEmpresa,date,idCategoria));
    }

    @PostMapping("/results")
    public ResponseEntity<?> companyGraphics(@RequestBody UsuarioResponseDTO usuarioResponseDTO) throws Exception {
        return ResponseEntity.ok(puntuacionTotalServiceImpl.getPuntuacionTotalForCharts(usuarioResponseDTO));
    }

}
