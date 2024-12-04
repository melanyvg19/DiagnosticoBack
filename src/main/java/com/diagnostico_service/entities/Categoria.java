package com.diagnostico_service.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categoria")
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "numero_preguntas")
    private Integer numeroPreguntas;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @OneToMany(targetEntity = Pregunta.class,mappedBy = "categoria",fetch = FetchType.LAZY)
    @JsonManagedReference(value = "categoria-pregunta")
    private List<Pregunta> preguntasList;

    @OneToMany(targetEntity = PreguntaRepetida.class,mappedBy = "categoria",fetch = FetchType.LAZY)
    @JsonManagedReference(value = "categoria-preguntarepetida")
    private List<PreguntaRepetida> preguntaRepetidaList;

    @OneToMany(targetEntity = Formulario.class,mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "formulario-categoria")
    private List<Formulario> formulariosList;

    @OneToMany(targetEntity = PuntuacionTotal.class, mappedBy = "categoria",fetch = FetchType.LAZY)
    private List<PuntuacionTotal> puntuacionTotalList;

    @OneToMany(targetEntity = Observacion.class, mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonManagedReference("categoria_observacion")
    private List<Observacion> observacionesList;
}
