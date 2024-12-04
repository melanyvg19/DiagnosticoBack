package com.diagnostico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDateFormDTO {

    private String nitEmpresa;
    private Date fechaAplicacion;

}
