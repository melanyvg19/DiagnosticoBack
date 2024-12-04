package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FormularioRequestDTO {

    private int idCategoria;
    private Map<Integer, Object> respuestas;
    private String observacion;

}
