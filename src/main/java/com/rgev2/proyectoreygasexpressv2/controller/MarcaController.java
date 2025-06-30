package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.MarcaDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MarcaEstadoUpdateDTO; // Nuevo DTO para actualizar estado
import com.rgev2.proyectoreygasexpressv2.service.MarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @PostMapping("/dueno/registerMarca")
    public ResponseEntity<MarcaDTO> registrarMarca(@RequestBody MarcaDTO marcaDTO) {
        MarcaDTO nuevaMarca = marcaService.registrarMarca(marcaDTO);
        return new ResponseEntity<>(nuevaMarca, HttpStatus.CREATED);
    }

    @GetMapping("/marcasActivas")
    public ResponseEntity<List<MarcaDTO>> listarMarcasActivas() {
        List<MarcaDTO> marcas = marcaService.listarTodasLasMarcasActivas();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping ("/dueno/marcas")
    public ResponseEntity<List<MarcaDTO>> listarMarcas(@RequestParam(required = false) String estado) {
        List<MarcaDTO> marcas = marcaService.listarTodasLasMarcas();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/obtenerMarcaId/{id}")
    public ResponseEntity<MarcaDTO> obtenerMarcaPorId(@PathVariable Integer id) {
        MarcaDTO marca = marcaService.obtenerMarcaPorId(id);
        return ResponseEntity.ok(marca);
    }

    @PutMapping("/dueno/marcas/{id}/estado")
    public ResponseEntity<MarcaDTO> actualizarEstadoMarca(
            @PathVariable Integer id,
            @Valid @RequestBody MarcaEstadoUpdateDTO estadoUpdateDTO) {
        MarcaDTO updatedMarca = marcaService.actualizarEstadoMarca(id, estadoUpdateDTO);
        return ResponseEntity.ok(updatedMarca);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().contains("no encontrada") || e.getMessage().contains("no existe")) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>("Error: " + e.getMessage(), status);
    }
}