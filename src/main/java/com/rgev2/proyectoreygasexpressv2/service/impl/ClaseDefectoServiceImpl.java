package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.model.Clase_Defecto;
import com.rgev2.proyectoreygasexpressv2.repository.ClaseDefectoRepository;
import com.rgev2.proyectoreygasexpressv2.service.ClaseDefectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClaseDefectoServiceImpl implements ClaseDefectoService {

    private final ClaseDefectoRepository claseDefectoRepository;

    @Override
    public Clase_Defecto saveClaseDefecto(Clase_Defecto claseDefecto) {
        return claseDefectoRepository.save(claseDefecto);
    }
}