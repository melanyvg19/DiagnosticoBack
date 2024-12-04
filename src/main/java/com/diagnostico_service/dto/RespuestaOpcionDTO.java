package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaOpcionDTO {
    private int idOpcion;
    private String textoRespuesta;
}

