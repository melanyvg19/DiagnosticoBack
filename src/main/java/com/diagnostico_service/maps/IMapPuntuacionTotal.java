package com.diagnostico_service.maps;

import com.diagnostico_service.dto.PuntuacionTotalResponseDTO;
import com.diagnostico_service.entities.PuntuacionTotal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapPuntuacionTotal {

    @Mappings({
            @Mapping(source = "puntuacionTotal", target = "puntuacionTotal"),
            @Mapping(source = "fecha",target = "fecha"),
            @Mapping(source = "empresa.nitEmpresa",target = "nombreCategoria"),
            @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    })
    PuntuacionTotalResponseDTO mapPuntuacionTotal(PuntuacionTotal puntuacionTotal);

    @Mappings({
            @Mapping(source = "puntuacionTotal", target = "puntuacionTotal"),
            @Mapping(source = "fecha",target = "fecha"),
            @Mapping(source = "empresa.nitEmpresa",target = "nombreCategoria"),
            @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    })
    List<PuntuacionTotalResponseDTO> mapPuntuacionTotalList(List<PuntuacionTotal> puntuacionTotalList);

}
