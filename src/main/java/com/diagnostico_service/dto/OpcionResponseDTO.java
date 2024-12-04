package com.diagnostico_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpcionResponseDTO {

    private Integer idPregunta;
    private Integer idOpcion;
    private String textoOpcion;
    private Float puntuacion;
    private Integer idTipoOpcion;

    public OpcionResponseDTO(Integer idRespidOpcionuesta, Float puntuacion, String textoOpcion, Integer idPregunta, Integer idTipoOpcion) {
        this.idPregunta = idPregunta;
        this.idOpcion = idRespidOpcionuesta;
        this.textoOpcion = textoOpcion;
        this.puntuacion = puntuacion;
        this.idTipoOpcion = idTipoOpcion;
    }

}
