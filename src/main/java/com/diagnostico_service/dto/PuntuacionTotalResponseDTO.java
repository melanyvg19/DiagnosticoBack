package com.diagnostico_service.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PuntuacionTotalResponseDTO {

    private Float puntuacionTotal;
    private String nombreCategoria;
    private Integer idCategoria;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

}
