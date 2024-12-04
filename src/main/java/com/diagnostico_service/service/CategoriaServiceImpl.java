package com.diagnostico_service.service;

import com.diagnostico_service.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public String getNombreCatgoriaByIdCategoria(Integer idCategoria){
        return categoriaRepository.getNombreCategoriaByIdCategoria(idCategoria);
    }
}
