package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.DistritoDTO;
import com.rgev2.proyectoreygasexpressv2.model.Distrito;
import com.rgev2.proyectoreygasexpressv2.repository.DistritoRepository;
import com.rgev2.proyectoreygasexpressv2.service.DistritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistritoServiceImpl implements DistritoService {

    @Autowired
    private DistritoRepository distritoRepository;

    private DistritoDTO mapToDistritoDTO(Distrito distrito) {
        if (distrito == null) {
            return null;
        }
        DistritoDTO dto = new DistritoDTO();
        dto.setIdDistrito(distrito.getIdDistrito());
        dto.setNombreDistrito(distrito.getNombreDistrito());
        dto.setZonaDistrito(distrito.getZonaDistrito());
        return dto;
    }

    private Distrito mapToDistritoEntity(DistritoDTO dto) {
        if (dto == null) {
            return null;
        }
        Distrito distrito = new Distrito();
        distrito.setNombreDistrito(dto.getNombreDistrito());
        distrito.setZonaDistrito(dto.getZonaDistrito());
        return distrito;
    }

    @Override
    public DistritoDTO createDistrito(DistritoDTO distritoDTO) {
        distritoRepository.findByNombreDistrito(distritoDTO.getNombreDistrito()).ifPresent(d -> {
            throw new RuntimeException("Ya existe un distrito con el nombre: " + distritoDTO.getNombreDistrito());
        });

        Distrito distrito = mapToDistritoEntity(distritoDTO);
        Distrito savedDistrito = distritoRepository.save(distrito);
        return mapToDistritoDTO(savedDistrito);
    }

    @Override
    public DistritoDTO getDistritoById(Integer id) {
        Distrito distrito = distritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con ID: " + id));
        return mapToDistritoDTO(distrito);
    }

    @Override
    public List<DistritoDTO> getAllDistritos() {
        List<Distrito> distritos = distritoRepository.findAll();
        // Mapea cada entidad Distrito a un DistritoDTO y los recolecta en una lista
        return distritos.stream()
                .map(this::mapToDistritoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DistritoDTO updateDistrito(Integer id, DistritoDTO distritoDTO) {
        Distrito existingDistrito = distritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con ID: " + id));

        if (distritoDTO.getNombreDistrito() != null && !distritoDTO.getNombreDistrito().isBlank()) {
            existingDistrito.setNombreDistrito(distritoDTO.getNombreDistrito());
        }
        if (distritoDTO.getZonaDistrito() != null && !distritoDTO.getZonaDistrito().isBlank()) {
            existingDistrito.setZonaDistrito(distritoDTO.getZonaDistrito());
        }

        Distrito updatedDistrito = distritoRepository.save(existingDistrito);
        return mapToDistritoDTO(updatedDistrito);
    }

    @Override
    public void deleteDistrito(Integer id) {
        if (!distritoRepository.existsById(id)) {
            throw new RuntimeException("Distrito no encontrado con ID: " + id);
        }
        distritoRepository.deleteById(id);
    }
}