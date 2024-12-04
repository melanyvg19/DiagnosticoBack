package com.diagnostico_service.maps;

import com.diagnostico_service.dto.GlosarioDTO;
import com.diagnostico_service.entities.Glosario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapGlosario {

    @Mappings({
            @Mapping(source = "idGlosario",target = "idGlosario"),
            @Mapping(source = "nombreGlosario",target = "nombreGlosario"),
            @Mapping(source = "textoGlosario", target = "textoGlosario"),
            @Mapping(source = "imagen", target = "imagen")
    })
    GlosarioDTO mapGlosario(Glosario glosario);

    @Mappings({
            @Mapping(source = "idGlosario",target = "idGlosario"),
            @Mapping(source = "nombreGlosario",target = "nombreGlosario"),
            @Mapping(source = "textoGlosario", target = "textoGlosario"),
            @Mapping(source = "imagen", target = "imagen")
    })
    List<GlosarioDTO> mapGlosarioList(List<Glosario> glosarioList);

}
