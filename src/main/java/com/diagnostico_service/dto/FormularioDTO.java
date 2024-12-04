package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormularioDTO {

    private Integer idEmpresa;
    private Integer idCategoria;
    private Integer idPregunta;
    private Integer idOpcion;
}
