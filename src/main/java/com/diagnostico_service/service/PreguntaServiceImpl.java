package com.diagnostico_service.service;

import com.diagnostico_service.dto.OpcionResponseDTO;
import com.diagnostico_service.dto.PreguntaRequestDTO;
import com.diagnostico_service.dto.PreguntaRespuestaDTO;
import com.diagnostico_service.entities.Categoria;
import com.diagnostico_service.entities.Pregunta;
import com.diagnostico_service.entities.TipoPregunta;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.maps.IMapOpcion;
import com.diagnostico_service.maps.IMapPregunta;
import com.diagnostico_service.repository.CategoriaRepository;
import com.diagnostico_service.repository.PreguntaRepository;
import com.diagnostico_service.repository.TipoOpcionRepository;
import com.diagnostico_service.repository.TipoPreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreguntaServiceImpl {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private IMapPregunta mapPregunta;

    @Autowired
    private IMapOpcion mapOpcion;

    @Autowired
    private TipoPreguntaRepository tipoPreguntaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<OpcionResponseDTO> getOpcionesByCategoria(Long idCategoria) throws ObjectNotFoundException {
        List<OpcionResponseDTO> opcionResponseDTOList = mapOpcion.mapOpcionList(preguntaRepository.findOpcionesByCategoria(idCategoria));
        if (opcionResponseDTOList.isEmpty()){
            throw new ObjectNotFoundException("La categoria con el id " + idCategoria +" no existe");
        }
        return opcionResponseDTOList;
    }

    public List<PreguntaRequestDTO> getPreguntaByCategoria(Long idCategoria) throws ObjectNotFoundException {
        List<PreguntaRequestDTO> preguntaRequestDTOList = mapPregunta.mapPreguntaList(preguntaRepository.findByCategoria(idCategoria));
        if (preguntaRequestDTOList.isEmpty()){
            throw new ObjectNotFoundException("La categoria con el id " + idCategoria +" no existe");
        }
        return preguntaRequestDTOList;
    }

    public PreguntaRespuestaDTO getPreguntaRespuestas(Long idCategoria) throws ObjectNotFoundException {
        List<PreguntaRequestDTO> preguntaRequestDTOList = mapPregunta.mapPreguntaList(preguntaRepository.findByCategoria(idCategoria));
        List<OpcionResponseDTO> opcionResponseDTOList = mapOpcion.mapOpcionList(preguntaRepository.findOpcionesByCategoria(idCategoria));
        if (preguntaRequestDTOList.isEmpty() || opcionResponseDTOList.isEmpty()) {
            throw new ObjectNotFoundException("La categoria con el id " + idCategoria + " no existe");
        }

        return PreguntaRespuestaDTO.builder()
                .preguntaList(preguntaRequestDTOList)
                .opcionList(opcionResponseDTOList)
                .build();

    }

    public PreguntaRequestDTO addPreguntas(Pregunta pregunta) throws Exception {
        if (tipoPreguntaRepository.findById(pregunta.getTipoPregunta().getIdTipoPregunta()).isEmpty()){
            throw new ObjectNotFoundException("El tipo de pregunta no existe");
        }
        if (categoriaRepository.findById(pregunta.getCategoria().getIdCategoria()).isEmpty()){
            throw new ObjectNotFoundException("La categoria no existe");
        }
        return mapPregunta.mapPregunta(preguntaRepository.save(pregunta));
    }

    public Boolean deletePregunta(Integer id) throws ObjectNotFoundException {
        if (preguntaRepository.existsById(id)){
            preguntaRepository.deleteById(id);
            return true;
        }else {
            throw new ObjectNotFoundException("La pregunta con el id " + id + " no existe.");
        }
    }

    public PreguntaRequestDTO updatePregunta(PreguntaRequestDTO preguntaRequestDTO) throws ObjectNotFoundException {

        Optional<Pregunta> preguntaOptional = preguntaRepository.findById(preguntaRequestDTO.getIdPregunta());
        if (preguntaOptional.isPresent()){
            Pregunta pregunta = preguntaOptional.get();
            if (preguntaRequestDTO.getIdTipoPregunta() != null){
                Optional<TipoPregunta> tipoPreguntaOptional = tipoPreguntaRepository.findById(preguntaRequestDTO.getIdTipoPregunta());
                if (tipoPreguntaOptional.isEmpty()){
                    throw new ObjectNotFoundException("El tipo de pregunta no existe");
                }
                TipoPregunta tipoPregunta = tipoPreguntaOptional.get();
                pregunta.setTipoPregunta(tipoPregunta);
            }

            if (preguntaRequestDTO.getCategoria() != null){
                Optional<Categoria> categoriaOptional = categoriaRepository.findById(preguntaRequestDTO.getCategoria());
                if (categoriaOptional.isEmpty()){
                    throw new ObjectNotFoundException("La categoria no existe");
                }
                Categoria categoria = categoriaOptional.get();
                pregunta.setCategoria(categoria);
            }
            if (preguntaRequestDTO.getNombrePregunta() != null){
                pregunta.setNombrePregunta(preguntaRequestDTO.getNombrePregunta());
            }
            return mapPregunta.mapPregunta(preguntaRepository.save(pregunta));
        }else {
            throw new ObjectNotFoundException("La pregunta con el id " + preguntaRequestDTO.getIdPregunta() + " no existe");
        }

    }
}
