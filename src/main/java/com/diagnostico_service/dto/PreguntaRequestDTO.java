package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaRequestDTO {

    private Integer idPregunta;
    private String nombrePregunta;
    private Integer idTipoPregunta;
    private Integer categoria;

}
