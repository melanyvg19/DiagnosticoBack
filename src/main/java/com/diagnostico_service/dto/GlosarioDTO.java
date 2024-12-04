package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GlosarioDTO {

    private Integer idGlosario;
    private String nombreGlosario;
    private String textoGlosario;
    private String imagen;

}
