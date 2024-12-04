package com.diagnostico_service.service;

import com.diagnostico_service.dto.ChartsDTO;
import com.diagnostico_service.dto.PuntuacionTotalResponseDTO;
import com.diagnostico_service.dto.UsuarioResponseDTO;
import com.diagnostico_service.entities.PuntuacionTotal;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.maps.IMapPuntuacionTotal;
import com.diagnostico_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PuntuacionTotalServiceImpl {

    @Autowired
    private PuntuacionTotalRepository puntuacionTotalRepository;

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private IMapPuntuacionTotal mapPuntuacionTotal;

    @Autowired
    private PreguntaRepetidaRepository preguntaRepetidaRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ChartsDTO getPuntuacionTotalForCharts(String headers) throws Exception {
        try {
            String token = headers.substring(7);
            String nitEmpresa = jwtService.decodeToken(token);
            List<Integer> idCategoriaList = puntuacionTotalRepository.getIdCategoriasByNitEmpresaNotInPreguntaRepetida(nitEmpresa);
            List<PuntuacionTotalResponseDTO> puntuacionTotalResponseDTOList = new ArrayList<>();
            for (Integer idCategoria: idCategoriaList){

                PuntuacionTotal puntuacionTotal = puntuacionTotalRepository.getPuntuacionTotalByIdCategoria(idCategoria,nitEmpresa)
                        .orElseThrow(() -> new ObjectNotFoundException("No existe un puntuacion total"));
                Float puntuacionTotalValue = puntuacionTotal.getPuntuacionTotal();
                if (puntuacionTotalValue > 100){
                    puntuacionTotalValue = 100f;
                }
                puntuacionTotalResponseDTOList.add(PuntuacionTotalResponseDTO.builder()
                        .puntuacionTotal(puntuacionTotalValue)
                        .idCategoria(idCategoria)
                        .nombreCategoria(categoriaService.getNombreCatgoriaByIdCategoria(idCategoria))
                                .fecha(puntuacionTotal.getFecha())
                        .build());
            }
            return ChartsDTO.builder()
                    .puntuaciones(puntuacionTotalResponseDTOList)
                    .build();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public ChartsDTO getPuntuacionTotalForCharts(UsuarioResponseDTO usuarioResponseDTO) throws Exception {
        try {
            String nitEmpresa = usuarioRepository.findById(usuarioResponseDTO.getIdUsuario()).get().getEmpresa().getNitEmpresa();
            List<Integer> idCategoriaList = puntuacionTotalRepository.getIdCategoriasByNitEmpresaNotInPreguntaRepetida(nitEmpresa);
            List<PuntuacionTotalResponseDTO> puntuacionTotalResponseDTOList = new ArrayList<>();
            for (Integer idCategoria: idCategoriaList){

                PuntuacionTotal puntuacionTotal = puntuacionTotalRepository.getPuntuacionTotalByIdCategoria(idCategoria,nitEmpresa)
                        .orElseThrow(() -> new ObjectNotFoundException("No existe un puntuacion total"));
                Float puntuacionTotalValue = puntuacionTotal.getPuntuacionTotal();
                if (puntuacionTotalValue > 100){
                    puntuacionTotalValue = 100f;
                }
                puntuacionTotalResponseDTOList.add(PuntuacionTotalResponseDTO.builder()
                        .puntuacionTotal(puntuacionTotalValue)
                        .idCategoria(idCategoria)
                        .nombreCategoria(categoriaService.getNombreCatgoriaByIdCategoria(idCategoria))
                        .fecha(puntuacionTotal.getFecha())
                        .build());
            }
            return ChartsDTO.builder()
                    .puntuaciones(puntuacionTotalResponseDTOList)
                    .build();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public List<PuntuacionTotalResponseDTO> addPuntuacionTotal(String nitEmpresa, Date fecha){
        List<Integer> idCategoriaList = formularioRepository.findIdCategoriasByNitEmpresa(nitEmpresa);
        List<PuntuacionTotalResponseDTO> puntuacionTotalResponseDTOList = new ArrayList<>();
        for (Integer idCategoria:idCategoriaList){
            Integer numeroPreguntas = preguntaRepository.findCantidadPreguntaByCategoria(idCategoria);
            Float puntuacionTotal = (formularioRepository.findPuntuacionTotalByIdCategoria(idCategoria,nitEmpresa,fecha)/numeroPreguntas) * 100;
            puntuacionTotal = Math.round(puntuacionTotal * 100.0f) / 100.0f;
            PuntuacionTotalResponseDTO puntuacionTotalResponseDTO = mapPuntuacionTotal.mapPuntuacionTotal(puntuacionTotalRepository.save(PuntuacionTotal.builder()
                    .puntuacionTotal(puntuacionTotal)
                    .empresa(empresaRepository.findById(nitEmpresa).get())
                    .categoria(categoriaRepository.findById(idCategoria).get())
                    .fecha(fecha)
                    .build()));
            puntuacionTotalResponseDTOList.add(puntuacionTotalResponseDTO);
        }

        return puntuacionTotalResponseDTOList;
    }

    public ChartsDTO getPuntuacionTotalForChartsExtra(String headers) throws Exception {
        try {
            String token = headers.substring(7);
            String nitEmpresa = jwtService.decodeToken(token);
            List<Integer> idCategoriaList = preguntaRepetidaRepository.findIdCategoria();
            List<PuntuacionTotalResponseDTO> puntuacionTotalResponseDTOList = new ArrayList<>();
            for (Integer idCategoria: idCategoriaList){
                PuntuacionTotal puntuacionTotal = puntuacionTotalRepository.getPuntuacionTotalByIdCategoria(idCategoria,nitEmpresa)
                        .orElseThrow(() -> new ObjectNotFoundException("No existe un puntuacion total"));
                puntuacionTotalResponseDTOList.add(PuntuacionTotalResponseDTO.builder()
                        .puntuacionTotal(puntuacionTotal.getPuntuacionTotal())
                        .idCategoria(idCategoria)
                        .nombreCategoria(categoriaService.getNombreCatgoriaByIdCategoria(idCategoria))
                                .fecha(puntuacionTotal.getFecha())
                        .build());
            }
            return ChartsDTO.builder()
                    .puntuaciones(puntuacionTotalResponseDTOList)
                    .build();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

}
