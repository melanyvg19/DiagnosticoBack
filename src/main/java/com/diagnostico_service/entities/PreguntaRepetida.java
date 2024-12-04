package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "pregunta_repetida")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreguntaRepetida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta_repetida")
    private Integer idPreguntaRepetida;

    @ManyToOne()
    @JoinColumn(name = "id_pregunta", referencedColumnName = "id_pregunta")
    @JsonBackReference(value = "preguntarepetida-pregunta")
    private Pregunta pregunta;

    @ManyToOne()
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @JsonBackReference(value = "categoria-preguntarepetida")
    private Categoria categoria;

}
