package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpresaResponseDTO {

    private String nitEmpresa;
    private String nombreEmpresa;
    private String direccionPrincipal;
    private String estadoEmpresa;
    private Integer cantidadSedes;
    private Boolean establecimientosComerciales;
    private String sectorEconomico;
    private String alcanceComercial;
    private Boolean esAliado;
    private String tipoEmpresa;
}
