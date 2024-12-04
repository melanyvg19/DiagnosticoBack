package com.diagnostico_service.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pregunta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @Column(name = "nombre_pregunta")
    private String nombrePregunta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_pregunta", referencedColumnName = "id_tipo_pregunta")
    @JsonBackReference(value = "tipoPregunta-pregunta")
    private TipoPregunta tipoPregunta;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @JsonBackReference(value = "categoria-pregunta")
    private Categoria categoria;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "pregunta-opcion")
    private List<Opcion> opcionList;

    @OneToMany(mappedBy = "pregunta")
    @JsonManagedReference(value = "pregunta-formulario")
    private List<Formulario> formularioList;

    @OneToMany(mappedBy = "pregunta")
    @JsonManagedReference(value = "preguntarepetida-pregunta")
    private List<PreguntaRepetida> preguntaRepetida;


}
