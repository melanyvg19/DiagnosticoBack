package com.diagnostico_service.service;

import com.diagnostico_service.entities.TipoPregunta;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipoPreguntaServiceImpl {

    public Optional<TipoPregunta> findAll() {
        return Optional.empty();
    }
}
