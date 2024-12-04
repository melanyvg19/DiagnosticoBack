package com.diagnostico_service.maps;

import com.diagnostico_service.dto.PreguntaRequestDTO;
import com.diagnostico_service.entities.Pregunta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapPregunta {

    @Mappings({
            @Mapping(source = "idPregunta", target = "idPregunta"),
            @Mapping(source = "nombrePregunta",target = "nombrePregunta"),
            @Mapping(source = "tipoPregunta.idTipoPregunta", target = "idTipoPregunta"),
            @Mapping(source = "categoria.idCategoria", target = "categoria"),
    })
    PreguntaRequestDTO mapPregunta(Pregunta Pregunta);

    @Mappings({
            @Mapping(source = "idPregunta", target = "idPregunta"),
            @Mapping(source = "nombrePregunta",target = "nombrePregunta"),
            @Mapping(source = "tipoPregunta.idTipoPregunta", target = "idTipoPregunta"),
            @Mapping(source = "categoria.idCategoria", target = "categoria"),
    })
    List<PreguntaRequestDTO> mapPreguntaList(List<Pregunta> preguntaList);
}
