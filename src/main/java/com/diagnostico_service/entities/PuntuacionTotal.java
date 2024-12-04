package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "puntuacion_total")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PuntuacionTotal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puntuacion_total")
    private Long idPuntuacionTotal;

    @Column(name = "puntuacion_total")
    private Float puntuacionTotal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_aplicacion")
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "nit_empresa")
    @JsonBackReference(value = "empresa-puntuaciontotal")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}

