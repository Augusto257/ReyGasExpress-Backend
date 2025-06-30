package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.MarcaDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MarcaEstadoUpdateDTO;
import com.rgev2.proyectoreygasexpressv2.model.Marca;
import com.rgev2.proyectoreygasexpressv2.repository.MarcaRepository;
import com.rgev2.proyectoreygasexpressv2.service.MarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;

    private MarcaDTO mapToMarcaDTO(Marca marca) {
        if (marca == null) {
            return null;
        }
        return new MarcaDTO(marca.getIdMarca(), marca.getNombreMarca(), marca.getTipoMarca(),marca.getEstadoMarca());
    }

    @Override
    public MarcaDTO registrarMarca(MarcaDTO marcaDTO) {

        marcaRepository.findByNombreMarcaIgnoreCase(marcaDTO.getNombreMarca()).ifPresent(m -> {
            throw new IllegalArgumentException("Ya existe una marca con el nombre: " + marcaDTO.getNombreMarca());
        });

        Marca marca = new Marca();
        marca.setNombreMarca(marcaDTO.getNombreMarca());
        marca.setTipoMarca(marcaDTO.getTipo().toUpperCase());

        marca.setEstadoMarca(marcaDTO.getEstadoMarca() != null ? marcaDTO.getEstadoMarca().toUpperCase() : "ACTIVO");

        Marca marcaGuardada = marcaRepository.save(marca);
        return mapToMarcaDTO(marcaGuardada);
    }

    @Override
    public List<MarcaDTO> listarTodasLasMarcasActivas() {
        return marcaRepository.findByEstadoMarcaIgnoreCase("ACTIVO").stream()
                .map(this::mapToMarcaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarcaDTO> listarTodasLasMarcas() {
        return marcaRepository.findAll().stream()
                .map(this::mapToMarcaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MarcaDTO obtenerMarcaPorId(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));
        return mapToMarcaDTO(marca);
    }

    @Override
    public MarcaDTO actualizarEstadoMarca(Integer idMarca, MarcaEstadoUpdateDTO estadoUpdateDTO) {
        Marca existingMarca = marcaRepository.findById(idMarca)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + idMarca));

        String nuevoEstado = estadoUpdateDTO.getEstadoMarca().toUpperCase();

        if (!"ACTIVO".equals(nuevoEstado) && !"INACTIVO".equals(nuevoEstado)) {
            throw new IllegalArgumentException("Estado de marca inválido. Solo se permite 'ACTIVO' o 'INACTIVO'.");
        }

        existingMarca.setEstadoMarca(nuevoEstado);
        Marca updatedMarca = marcaRepository.save(existingMarca);
        return mapToMarcaDTO(updatedMarca);
    }
}