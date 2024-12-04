package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "conjunto_categoria")
@AllArgsConstructor
@NoArgsConstructor
public class ConjuntoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conjunto_categoria")
    private Long idConjuntoCategoria;

    @Column(name = "nombre_conjunto_categoria")
    private String nombreConjuntoCategoria;

    @Column(name = "puntuacion_total")
    private Float puntuacionTotal;

    @Column(name = "fecha_aplicacion")
    private LocalDate fechaAplicacion;

    @ManyToOne
    @JoinColumn(name = "nit_empresa")
    @JsonBackReference(value = "empresa-conjuntocategoria")
    private Empresa empresa;

}
