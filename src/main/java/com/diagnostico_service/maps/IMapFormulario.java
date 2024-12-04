package com.diagnostico_service.maps;

import com.diagnostico_service.dto.FormularioDTO;
import com.diagnostico_service.entities.Formulario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapFormulario {

    @Mappings({
            @Mapping(source = "empresa.nitEmpresa", target = "idEmpresa"),
            @Mapping(source = "categoria.idCategoria",target = "idCategoria"),
            @Mapping(source = "pregunta.idPregunta", target = "idPregunta"),
            @Mapping(source = "opcion.idOpcion", target = "idOpcion")
    })
    FormularioDTO mapFormulario(Formulario formulario);

    @Mappings({
            @Mapping(source = "empresa.nitEmpresa", target = "idEmpresa"),
            @Mapping(source = "categoria.idCategoria",target = "idCategoria"),
            @Mapping(source = "pregunta.idPregunta", target = "idPregunta"),
            @Mapping(source = "opcion.idOpcion", target = "idOpcion")
    })
    List<FormularioDTO> mapFormularioList(List<Formulario> formularioList);

}
