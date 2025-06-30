package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.MarcaDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MarcaEstadoUpdateDTO;
import java.util.List;

public interface MarcaService {
    MarcaDTO registrarMarca(MarcaDTO marcaDTO);

    List<MarcaDTO> listarTodasLasMarcasActivas();

    List<MarcaDTO> listarTodasLasMarcas();

    MarcaDTO obtenerMarcaPorId(Integer id);

    MarcaDTO actualizarEstadoMarca(Integer idMarca, MarcaEstadoUpdateDTO estadoUpdateDTO);
}