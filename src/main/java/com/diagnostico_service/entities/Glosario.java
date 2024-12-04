package com.diagnostico_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "glosario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Glosario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_glosario")
    private Integer idGlosario;

    @Column(name = "nombre_glosario")
    private String nombreGlosario;

    @Column(name = "texto_glosario", length = 1000)
    private String textoGlosario;

    @Column(name = "imagen")
    private String imagen;


}
