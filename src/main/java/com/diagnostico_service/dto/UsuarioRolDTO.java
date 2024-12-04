package com.diagnostico_service.dto;

import com.diagnostico_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRolDTO {

    private Integer idUsuario;
    private Role role;

}
