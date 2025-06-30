package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.DetallePedidoDTO;
import com.rgev2.proyectoreygasexpressv2.service.DetallePedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @GetMapping("/detallePedido/{id}")
    public ResponseEntity<DetallePedidoDTO> obtenerDetallePedidoPorId(@PathVariable Integer id) {
        return detallePedidoService.obtenerDetallePedidoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/detallePedido/porPedido/{idPedido}")
    public ResponseEntity<List<DetallePedidoDTO>> obtenerDetallesPorPedido(@PathVariable Integer idPedido) {
        List<DetallePedidoDTO> detalles = detallePedidoService.obtenerDetallesPorPedido(idPedido);
        return ResponseEntity.ok(detalles);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().toLowerCase().contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), status);
    }
}