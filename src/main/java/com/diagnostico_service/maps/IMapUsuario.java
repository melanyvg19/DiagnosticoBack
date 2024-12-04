package com.diagnostico_service.maps;

import com.diagnostico_service.dto.UsuarioResponseDTO;
import com.diagnostico_service.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapUsuario {

    @Mappings({
            @Mapping(source = "idUsuario", target = "idUsuario"),
            @Mapping(source = "nombreCompleto",target = "nombreCompleto"),
            @Mapping(source = "cargo", target = "cargo"),
            @Mapping(source = "estadoUsuario", target = "estadoUsuario"),
            @Mapping(source = "aniosVinculado", target = "aniosVinculado"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "formTerminado", target = "formTerminado"),
            @Mapping(source = "supervisor.empresa.nombreEmpresa", target = "supervisor"),
            @Mapping(source = "empresa.nombreEmpresa", target = "empresa")
    })
    UsuarioResponseDTO mapUsuario(Usuario usuario);

    @Mappings({
            @Mapping(source = "idUsuario", target = "idUsuario"),
            @Mapping(source = "nombreCompleto",target = "nombreCompleto"),
            @Mapping(source = "cargo", target = "cargo"),
            @Mapping(source = "estadoUsuario", target = "estadoUsuario"),
            @Mapping(source = "aniosVinculado", target = "aniosVinculado"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "formTerminado", target = "formTerminado"),
            @Mapping(source = "supervisor.empresa.nombreEmpresa", target = "supervisor"),
            @Mapping(source = "empresa.nombreEmpresa", target = "empresa")
    })
    List<UsuarioResponseDTO> mapUsuarioList(List<Usuario> usuarioList);

}
