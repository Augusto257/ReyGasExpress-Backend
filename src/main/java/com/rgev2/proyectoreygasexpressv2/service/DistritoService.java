package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.DistritoDTO;
import java.util.List;

public interface DistritoService {

    DistritoDTO createDistrito(DistritoDTO distritoDTO);

    DistritoDTO getDistritoById(Integer id);

    List<DistritoDTO> getAllDistritos();

    DistritoDTO updateDistrito(Integer id, DistritoDTO distritoDTO);

    void deleteDistrito(Integer id);
}