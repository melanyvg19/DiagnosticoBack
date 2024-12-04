package com.diagnostico_service.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tipo_opcion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoOpcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_opcion")
    private Integer idTipoOpcion;

    @Column(name = "tipo_opcion")
    private String tipoOpcion;

    @OneToMany(targetEntity = Opcion.class, mappedBy = "tipoOpcion",fetch = FetchType.LAZY)
    @JsonManagedReference(value = "pregunta-tipo-opcion")
    private List<Opcion> opcionList;

}
