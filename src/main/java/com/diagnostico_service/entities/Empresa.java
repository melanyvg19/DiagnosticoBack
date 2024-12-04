package com.diagnostico_service.entities;

import com.diagnostico_service.enums.TipoEmpresa;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    @Column(name = "nit_empresa")
    private String nitEmpresa;

    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

    @Column(name = "direccion_principal")
    private String direccionPrincipal;

    @Column(name = "estado_empresa", length = 10)
    private String estadoEmpresa;

    @Column(name = "cantidad_sedes")
    private Integer cantidadSedes;

    @Column(name = "establecimientos_comerciales", columnDefinition = "TINYINT")
    private Boolean establecimientosComerciales;

    @Column(name = "sector_economico")
    private String sectorEconomico;

    @Column(name = "alcance_comercial")
    private String alcanceComercial;

    @Column(name = "es_aliado", columnDefinition = "TINYINT")
    private Boolean esAliado;

    @Enumerated(value = EnumType.STRING)
    private TipoEmpresa tipoEmpresa;

    @OneToMany(targetEntity = Usuario.class, mappedBy = "empresa", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa-usuario")
    private List<Usuario> usuariosList;

    @OneToMany(targetEntity = ConjuntoCategoria.class, mappedBy = "empresa",fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa-conjuntocategoria")
    private List<ConjuntoCategoria> conjuntoCategoriaList;

    @OneToMany(targetEntity = Formulario.class, mappedBy = "empresa", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa-formulario")
    private List<Formulario> formularioList;

    @OneToMany(targetEntity = PuntuacionTotal.class, mappedBy = "empresa", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa-puntuaciontotal")
    private List<PuntuacionTotal> puntuacionTotalList;

    @OneToMany(targetEntity = Observacion.class, mappedBy = "empresa", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa_observacion")
    private List<Observacion> observacionList;

}
