package com.diagnostico_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private String token;

    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
