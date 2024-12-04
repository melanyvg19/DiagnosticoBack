package com.diagnostico_service.maps;

import com.diagnostico_service.dto.EmpresaResponseDTO;
import com.diagnostico_service.entities.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapEmpresa {

    @Mappings({
            @Mapping(source = "nitEmpresa", target = "nitEmpresa"),
            @Mapping(source = "nombreEmpresa",target = "nombreEmpresa"),
            @Mapping(source = "direccionPrincipal", target = "direccionPrincipal"),
            @Mapping(source = "estadoEmpresa", target = "estadoEmpresa"),
            @Mapping(source = "cantidadSedes", target = "cantidadSedes"),
            @Mapping(source = "establecimientosComerciales", target = "establecimientosComerciales"),
            @Mapping(source = "sectorEconomico", target = "sectorEconomico"),
            @Mapping(source = "tipoEmpresa", target = "tipoEmpresa")
    })
    EmpresaResponseDTO mapEmpresa(Empresa empresa);

    @Mappings({
            @Mapping(source = "nitEmpresa", target = "nitEmpresa"),
            @Mapping(source = "nombreEmpresa",target = "nombreEmpresa"),
            @Mapping(source = "direccionPrincipal", target = "direccionPrincipal"),
            @Mapping(source = "estadoEmpresa", target = "estadoEmpresa"),
            @Mapping(source = "cantidadSedes", target = "cantidadSedes"),
            @Mapping(source = "establecimientosComerciales", target = "establecimientosComerciales"),
            @Mapping(source = "sectorEconomico", target = "sectorEconomico"),
            @Mapping(source = "tipoEmpresa", target = "tipoEmpresa")
    })
    List<EmpresaResponseDTO> mapEmpresaList(List<Empresa> empresaList);

}
