package com.diagnostico_service.service;

import com.diagnostico_service.dto.GlosarioDTO;
import com.diagnostico_service.entities.Glosario;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.maps.IMapGlosario;
import com.diagnostico_service.repository.GlosarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GlosarioServiceImpl {

    @Autowired
    private GlosarioRepository glosarioRepository;

    @Autowired
    private IMapGlosario mapGlosario;

    public List<GlosarioDTO> allGlosarios() {
        return mapGlosario.mapGlosarioList(glosarioRepository.findAll());
    }

    public GlosarioDTO addGlosario(Glosario glosario){
        if (glosario.getNombreGlosario() == null || glosario.getNombreGlosario().isEmpty()){
            throw new BadUserCredentialsException("El nombre de glosario no puede ser nulo");
        }
        if (glosario.getTextoGlosario() == null || glosario.getTextoGlosario().isEmpty()){
            throw new BadUserCredentialsException("El texto del glosario no puede estar vacio");
        }
        if (glosario.getImagen() == null || glosario.getImagen().isEmpty()){
            throw new BadUserCredentialsException("La imagen del glosario no puede estar vacio");
        }
        return mapGlosario.mapGlosario(glosarioRepository.save(glosario));
    }

    public Boolean deleteGlosario(Integer id) throws ObjectNotFoundException {
        if (glosarioRepository.existsById(id)){
            glosarioRepository.deleteById(id);
            return true;
        }else {
            throw new ObjectNotFoundException("El termino de glosario con id " + id + " no existe.");
        }
    }

    public GlosarioDTO updateGlosario(GlosarioDTO glosarioDTO) throws ObjectNotFoundException {
       Optional<Glosario> glosarioOptional = glosarioRepository.findById(glosarioDTO.getIdGlosario());
        if (glosarioOptional.isPresent()){
            Glosario glosario = glosarioOptional.get();
            if (glosarioDTO.getNombreGlosario() != null && !glosarioDTO.getNombreGlosario().isEmpty()){
                glosario.setNombreGlosario(glosarioDTO.getNombreGlosario());
            }

            if (glosarioDTO.getTextoGlosario() != null && !glosarioDTO.getTextoGlosario().isEmpty()){
                glosario.setTextoGlosario(glosarioDTO.getTextoGlosario());
            }

            if (glosarioDTO.getImagen() != null && !glosarioDTO.getImagen().isEmpty()){
                glosario.setImagen(glosarioDTO.getImagen());
            }
            return mapGlosario.mapGlosario(glosarioRepository.save(glosario));
        }else {
            throw new ObjectNotFoundException("El termino del glosario con id " + glosarioDTO.getIdGlosario() + " no existe.");
        }

    }
}
