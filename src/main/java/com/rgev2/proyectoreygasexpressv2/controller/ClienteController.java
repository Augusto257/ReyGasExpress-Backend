package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.ClienteDTO;
import com.rgev2.proyectoreygasexpressv2.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/atencion/registerCliente")
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = clienteService.registrarCliente(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Integer id) {
        return clienteService.obtenerClientePorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarCliente")
    public ResponseEntity<ClienteDTO> buscarClientePorNombreApellidoDireccion(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String direccion) {
        return clienteService.buscarClientePorNombreApellidoDireccion(nombre, apellido, direccion)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/listarClientes")
    public ResponseEntity<List<ClienteDTO>> listarTodosLosClientes() {
        List<ClienteDTO> clientes = clienteService.listarTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST; // Para datos inválidos o duplicados
        } else if (e.getMessage().toLowerCase().contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND; // Para recursos no encontrados
        }
        return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), status);
    }
}