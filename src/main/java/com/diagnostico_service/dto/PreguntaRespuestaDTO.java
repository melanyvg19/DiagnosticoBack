package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PreguntaRespuestaDTO {

    private List<PreguntaRequestDTO> preguntaList;
    private List<OpcionResponseDTO> opcionList;

}
