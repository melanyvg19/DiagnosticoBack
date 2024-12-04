package com.diagnostico_service.service;

import com.diagnostico_service.dto.EmpresaResponseDTO;
import com.diagnostico_service.entities.Empresa;
import com.diagnostico_service.exceptions.CompanyAlreadyRegisterException;
import com.diagnostico_service.maps.IMapEmpresa;
import com.diagnostico_service.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaServiceImpl {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private IMapEmpresa iMapEmpresa;

    public EmpresaResponseDTO saveEmpresa(Empresa empresa) {
        if (empresaRepository.findEmpresaByNitEmpresa(empresa.getNitEmpresa()).isPresent()) {
            throw new CompanyAlreadyRegisterException("Ya hay una empresa con este Nit");
        }
        empresa.setEstadoEmpresa("Activa");
        return iMapEmpresa.mapEmpresa(empresaRepository.save(empresa));
    }
}
