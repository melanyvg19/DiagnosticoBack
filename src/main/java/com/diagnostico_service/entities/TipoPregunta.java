package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tipo_pregunta")
@AllArgsConstructor
@NoArgsConstructor
public class TipoPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pregunta")
    private Integer idTipoPregunta;

    @Column(name = "nombre_tipo")
    private String nombreTipoPregunta;

    @OneToMany(mappedBy = "tipoPregunta")
    @JsonManagedReference(value = "tipoPregunta-pregunta")
    private List<Pregunta> preguntaList;

}
