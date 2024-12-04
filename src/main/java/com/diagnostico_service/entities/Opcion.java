package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "opcion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Integer idOpcion;

    @Column(name = "Texto_opcion")
    private String textoOpcion;

    @Column(name = "puntuacion")
    private Float puntuacion;

    @ManyToOne
    @JoinColumn(name = "id_pregunta")
    @JsonBackReference(value = "pregunta-opcion")
    private Pregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_opcion")
    @JsonBackReference(value = "pregunta-tipo-opcion")
    private TipoOpcion tipoOpcion;

    @OneToMany(targetEntity = Formulario.class, mappedBy = "opcion", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "formulario-opcion")
    private List<Formulario> formularioList;

    @OneToMany(targetEntity = RespuestaAbierta.class, mappedBy = "opcion", fetch = FetchType.LAZY)
    private List<RespuestaAbierta> respuestaAbiertaList;

}
