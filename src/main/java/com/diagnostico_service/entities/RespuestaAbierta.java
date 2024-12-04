package com.diagnostico_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "respuesta_abierta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RespuestaAbierta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta_abierta")
    private Integer idRespuestaAbierta;

    @ManyToOne
    @JoinColumn(name = "id_formulario")
    private Formulario formulario;

    @ManyToOne
    @JoinColumn(name = "id_opcion")
    private Opcion opcion;

    @Column(name = "respuesta_texto")
    private String respuestaTexto;

}

