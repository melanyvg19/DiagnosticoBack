package com.diagnostico_service.dto;

import com.diagnostico_service.enums.EstadoUsuario;
import com.diagnostico_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nombreCompleto;
    private String cargo;
    private EstadoUsuario estadoUsuario;
    private Integer aniosVinculado;
    private Role role;
    private String supervisor;
    private String empresa;
    private String username;
    private Boolean formTerminado;

}
