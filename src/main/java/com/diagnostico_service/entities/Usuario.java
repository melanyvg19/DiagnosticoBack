package com.diagnostico_service.entities;

import com.diagnostico_service.enums.EstadoUsuario;
import com.diagnostico_service.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre_completo_usuario")
    private String nombreCompleto;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "correo_usuario")
    private String username;

    @Column(name = "contrasena")
    private String password;

    @Enumerated(value = EnumType.STRING)
    private EstadoUsuario estadoUsuario;

    @Column(name = "anios_vinculado")
    private Integer aniosVinculado;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "form_terminado", nullable = false)
    private Boolean formTerminado;

    @ManyToOne
    @JoinColumn(name = "id_supervisor")
    private Usuario supervisor;

    @ManyToOne
    @JoinColumn(name = "nit_empresa")
    @JsonBackReference(value = "empresa-usuario")
    private Empresa empresa;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private List<Usuario> clientes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
