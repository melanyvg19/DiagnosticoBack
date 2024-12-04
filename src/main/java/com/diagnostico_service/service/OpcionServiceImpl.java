package com.diagnostico_service.service;

import com.diagnostico_service.dto.OpcionResponseDTO;
import com.diagnostico_service.entities.Opcion;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.maps.IMapOpcion;
import com.diagnostico_service.repository.OpcionRepository;
import com.diagnostico_service.repository.PreguntaRepository;
import com.diagnostico_service.repository.TipoOpcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OpcionServiceImpl {

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private IMapOpcion mapOpcion;

    @Autowired
    private TipoOpcionRepository tipoOpcionRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    public Optional<Opcion> findAll() {
        return Optional.empty();
    }

    public OpcionResponseDTO addOpcion(Opcion opcion) throws Exception {
        if (preguntaRepository.findById(opcion.getPregunta().getIdPregunta()).isEmpty()){
            throw new ObjectNotFoundException("Pregunta no encontrada.");
        }
        if (opcion.getPuntuacion() < 0 || opcion.getPuntuacion() > 1){
            throw new BadUserCredentialsException("La puntuacion tiene que ser entre 0 y 1.");
        }
        if (tipoOpcionRepository.findById(opcion.getTipoOpcion().getIdTipoOpcion()).isEmpty()){
            throw new ObjectNotFoundException("Tipo de opcion no encontrada.");
        }
        return mapOpcion.mapOpcion(opcionRepository.save(opcion));
    }

    public Boolean deleteOpcion(Integer id) throws ObjectNotFoundException {
        if (opcionRepository.existsById(id)){
            opcionRepository.deleteById(id);
            return true;
        }else {
            throw new ObjectNotFoundException("La opcion con el id " + id + " no existe.");
        }
    }

    public OpcionResponseDTO updateOpcion(OpcionResponseDTO opcionResponseDTO) throws ObjectNotFoundException {
        Optional<Opcion> opcionOptional = opcionRepository.findById(opcionResponseDTO.getIdOpcion());
        if (opcionOptional.isPresent()){
            Opcion opcion = opcionOptional.get();
            if (opcionResponseDTO.getTextoOpcion() != null){
                opcion.setTextoOpcion(opcionResponseDTO.getTextoOpcion());
            }

            if (opcionResponseDTO.getPuntuacion() != null){
                if (opcionResponseDTO.getPuntuacion() < 0 || opcionResponseDTO.getPuntuacion() > 1){
                    throw new BadUserCredentialsException("La puntuacion tiene que ser entre 0 y 1.");
                }
                opcion.setPuntuacion(opcionResponseDTO.getPuntuacion());
            }
            if (opcionResponseDTO.getIdTipoOpcion() != null){
                if (tipoOpcionRepository.findById(opcionResponseDTO.getIdTipoOpcion()).isPresent()){
                    opcion.setTipoOpcion(tipoOpcionRepository.findById(opcionResponseDTO.getIdTipoOpcion()).get());
                }else {
                    throw new ObjectNotFoundException("Tipo de opcion no encontrado.");
                }
            }
            return mapOpcion.mapOpcion(opcionRepository.save(opcion));
        }else {
            throw new ObjectNotFoundException("La opcion con el id " + opcionResponseDTO.getIdOpcion() + " no existe.");
        }

    }
}
