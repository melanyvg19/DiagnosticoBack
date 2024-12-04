package com.diagnostico_service.maps;

import com.diagnostico_service.dto.OpcionResponseDTO;
import com.diagnostico_service.entities.Opcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapOpcion {

    @Mappings({
            @Mapping(source = "pregunta.idPregunta", target = "idPregunta"),
            @Mapping(source = "idOpcion",target = "idOpcion"),
            @Mapping(source = "textoOpcion", target = "textoOpcion"),
            @Mapping(source = "puntuacion", target = "puntuacion"),
            @Mapping(source = "tipoOpcion.idTipoOpcion", target = "idTipoOpcion")
    })
    OpcionResponseDTO mapOpcion(Opcion opcion);

    @Mappings({
            @Mapping(source = "pregunta.idPregunta", target = "idPregunta"),
            @Mapping(source = "idOpcion",target = "idOpcion"),
            @Mapping(source = "textoOpcion", target = "textoOpcion"),
            @Mapping(source = "puntuacion", target = "puntuacion"),
            @Mapping(source = "tipoOpcion.idTipoOpcion", target = "idTipoOpcion")
    })
    List<OpcionResponseDTO> mapOpcionList(List<Opcion> opcionList);

}
