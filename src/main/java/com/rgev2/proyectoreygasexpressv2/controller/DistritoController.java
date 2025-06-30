package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.DistritoDTO;
import com.rgev2.proyectoreygasexpressv2.service.DistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DistritoController {

    @Autowired
    private DistritoService distritoService;

    @PostMapping("/auth/crearDistrito")
    public ResponseEntity<DistritoDTO> createDistrito(@RequestBody DistritoDTO distritoDTO) {
        DistritoDTO createdDistrito = distritoService.createDistrito(distritoDTO);
        return new ResponseEntity<>(createdDistrito, HttpStatus.CREATED);
    }

    @GetMapping("/distritosID/{id}")
    public ResponseEntity<DistritoDTO> getDistritoById(@PathVariable Integer id) {
        DistritoDTO distrito = distritoService.getDistritoById(id);
        return ResponseEntity.ok(distrito);
    }

    @GetMapping("/distritos")
    public ResponseEntity<List<DistritoDTO>> getAllDistritos() {
        List<DistritoDTO> distritos = distritoService.getAllDistritos();
        return ResponseEntity.ok(distritos);
    }

    @PutMapping("/actualizarDistrito/{id}")
    public ResponseEntity<DistritoDTO> updateDistrito(@PathVariable Integer id, @RequestBody DistritoDTO distritoDTO) {
        DistritoDTO updatedDistrito = distritoService.updateDistrito(id, distritoDTO);
        return ResponseEntity.ok(updatedDistrito);
    }

    @DeleteMapping("/eliminarDistrito/{id}")
    public ResponseEntity<Void> deleteDistrito(@PathVariable Integer id) {
        distritoService.deleteDistrito(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}