package com.diagnostico_service.service;

import com.diagnostico_service.dto.ChartsDTO;
import com.diagnostico_service.dto.FormularioRequestDTO;
import com.diagnostico_service.entities.*;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormularioServiceImpl {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private PuntuacionTotalServiceImpl puntuacionTotalService;

    @Autowired
    private PreguntaRepetidaRepository preguntaRepetidaRepository;

    @Autowired
    private RespuestaAbiertaRepository respuestaAbiertaRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObservacionRepository observacionRepository;

    @Transactional
    public ChartsDTO crearObjeto(List<FormularioRequestDTO> formularioRequestDTOList, String authorizationHeader) throws Exception {
            Date fecha = new Date();

            Usuario usuario = jwtService.getUsuarioFromAuthorizationHeader(authorizationHeader);

            if (usuario.getFormTerminado()){
                throw new BadUserCredentialsException("El usuario ya terminó un formulario");
            }
            String nitEmpresa = usuario.getEmpresa().getNitEmpresa();
            Optional<Empresa> empresaOptional = empresaRepository.findEmpresaByNitEmpresa(nitEmpresa);

            if (empresaOptional.isEmpty()) {
                throw new ObjectNotFoundException("Empresa no encontrada");
            }

            Empresa empresa = empresaOptional.get();

            List<Integer> categoriaIds = formularioRequestDTOList.stream()
                    .map(FormularioRequestDTO::getIdCategoria)
                    .distinct()
                    .collect(Collectors.toList());

            List<Integer> preguntaIds = formularioRequestDTOList.stream()
                    .flatMap(formularioRequestDTO -> formularioRequestDTO.getRespuestas().keySet().stream())
                    .distinct()
                    .collect(Collectors.toList());

            Map<Integer, Categoria> categorias = categoriaRepository.findByIdCategoriaIn(categoriaIds)
                    .stream()
                    .collect(Collectors.toMap(Categoria::getIdCategoria, c -> c));

            Map<Integer, Pregunta> preguntas = preguntaRepository.findByIdPreguntaIn(preguntaIds)
                    .stream()
                    .collect(Collectors.toMap(Pregunta::getIdPregunta, p -> p));
            Integer totalPreguntas = preguntaRepository.countAllPregunta();

            if (preguntas.size() < totalPreguntas) {
                throw new BadUserCredentialsException("Hay preguntas sin resolver");
            }

            Map<Integer, Opcion> opcionesCache = new HashMap<>();

            for (FormularioRequestDTO formularioRequestDTO : formularioRequestDTOList) {
                Categoria categoria = categorias.get(formularioRequestDTO.getIdCategoria());
                if (categoria == null) {
                    throw new ObjectNotFoundException("Categoría no encontrada");
                }

                for (Map.Entry<Integer, Object> entry : formularioRequestDTO.getRespuestas().entrySet()) {
                    Integer idPregunta = entry.getKey();
                    Object respuesta = entry.getValue();

                    Pregunta pregunta = preguntas.get(idPregunta);
                    if (pregunta == null) {
                        throw new ObjectNotFoundException("Pregunta no encontrada");
                    }

                    if (respuesta instanceof Integer idOpcion) {
                        // Manejo de respuesta simple (Integer)
                        saveOpcion(opcionesCache, idOpcion, idPregunta, empresa, pregunta, categoria, fecha);
                    } else if (respuesta instanceof List<?> opciones) {
                        // Manejo de respuestas múltiples o complejas
                        for (Object opcionObj : opciones) {
                            if (opcionObj instanceof Integer idOpcion) {
                                saveOpcion(opcionesCache, idOpcion, idPregunta, empresa, pregunta, categoria, fecha);
                            } else if (opcionObj instanceof Map) {
                                Map<String, Object> opcionMap = (Map<String, Object>) opcionObj;
                                Integer idOpcion = (Integer) opcionMap.get("idOpcion");
                                String textoRespuesta = (String) opcionMap.get("textoRespuesta");
                                saveOpcion(opcionesCache, idOpcion, idPregunta, empresa, pregunta, categoria, fecha);
                                Formulario formulario = formularioRepository.findLastFormulario();
                                if (textoRespuesta != null) {
                                    guardarRespuestaAbierta(formulario.getIdFormulario(), idOpcion, textoRespuesta);
                                }
                            }
                        }
                    }
                }
                if (!formularioRequestDTO.getObservacion().isEmpty()) {
                    observacionRepository.save(Observacion.builder()
                            .fecha(fecha)
                            .categoria(categoria)
                            .empresa(empresa)
                            .textoObservacion(formularioRequestDTO.getObservacion())
                            .build());
                }

            }
            puntuacionTotalService.addPuntuacionTotal(nitEmpresa, fecha);
            ChartsDTO chartsDTO = puntuacionTotalService.getPuntuacionTotalForCharts(authorizationHeader);
            usuario.setFormTerminado(true);
            return chartsDTO;
    }

    private void guardarRespuestaAbierta(Long idFormulario, Integer idOpcion, String textoRespuesta) {
        RespuestaAbierta respuestaAbierta = new RespuestaAbierta();
        respuestaAbierta.setFormulario(formularioRepository.findById(idFormulario).orElse(null));
        respuestaAbierta.setOpcion(opcionRepository.findById(idOpcion).orElse(null));
        respuestaAbierta.setRespuestaTexto(textoRespuesta);
        respuestaAbiertaRepository.save(respuestaAbierta);
    }

    private void saveOpcion(Map<Integer, Opcion> opcionesCache, Integer idOpcion,
                            Integer idPregunta, Empresa empresa, Pregunta pregunta,
                            Categoria categoria, Date fecha) throws ObjectNotFoundException {
        Opcion opcion = opcionesCache.computeIfAbsent(idOpcion, id -> opcionRepository.findById(id).orElse(null));
        if (opcion == null) {
            System.out.println(idOpcion);
            throw new ObjectNotFoundException("Opción no encontrada");
        }
        if (preguntaRepetidaRepository.existsPreguntaRepetidaByPreguntaIdPregunta(idPregunta)) {
            Integer idCategoriaRepetida = preguntaRepetidaRepository.findIdCategoriaByPreguntaIdPregunta(idPregunta);
            Categoria categoriaRepetida = categoriaRepository.findById(idCategoriaRepetida).orElse(null);
            guardarFormulario(empresa,categoriaRepetida,pregunta,opcion, fecha);
        }
        guardarFormulario(empresa, categoria, pregunta, opcion, fecha);
    }

    private void guardarFormulario (Empresa empresa, Categoria categoria, Pregunta pregunta, Opcion opcion, Date fecha){
        Formulario formulario = new Formulario();
        formulario.setEmpresa(empresa);
        formulario.setCategoria(categoria);
        formulario.setPregunta(pregunta);
        formulario.setOpcion(opcion);
        formulario.setFecha(fecha);
        formularioRepository.save(formulario);
    }

}
