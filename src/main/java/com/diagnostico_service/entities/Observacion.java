package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "observacion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Observacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion")
    private Integer idObservacion;

    @Column(name = "texto_observacion", length = 1000)
    private String textoObservacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_aplicacion")
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @JsonBackReference(value = "categoria_observacion")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "nit_empresa")
    @JsonBackReference(value = "empresa_observacion")
    private Empresa empresa;
}
