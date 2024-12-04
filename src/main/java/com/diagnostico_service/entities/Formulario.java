package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "formulario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formulario")
    private Long idFormulario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_aplicacion")
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "nit_empresa")
    @JsonBackReference(value = "empresa-formulario")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @JsonBackReference(value = "formulario-categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_pregunta")
    @JsonBackReference(value = "pregunta-formulario")
    private Pregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "id_opcion")
    @JsonBackReference(value = "formulario-opcion")
    private Opcion opcion;

    @OneToMany(targetEntity = RespuestaAbierta.class, mappedBy = "formulario", fetch = FetchType.LAZY)
    private List<RespuestaAbierta> respuestaAbiertaList;
}