package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.PedidoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.PedidoEstadoUpdateDTO;
import com.rgev2.proyectoreygasexpressv2.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/atencion/registerPedido")
    public ResponseEntity<PedidoDTO> registrarPedido(@RequestBody PedidoDTO pedidoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Integer idUsuarioLogueado = null;
        if (authentication.getPrincipal() instanceof UserDetails) {

            String correoUsuario = ((UserDetails) authentication.getPrincipal()).getUsername();

            if (authentication.getPrincipal() instanceof com.rgev2.proyectoreygasexpressv2.model.Usuario) {
                idUsuarioLogueado = ((com.rgev2.proyectoreygasexpressv2.model.Usuario) authentication.getPrincipal()).getIdUsuario();
            } else {

                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        PedidoDTO nuevoPedido = pedidoService.registrarPedido(pedidoDTO, idUsuarioLogueado);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Integer id) {
        return pedidoService.obtenerPedidoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/pedidos/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstadoPedido(
            @PathVariable Integer id,
            @RequestBody PedidoEstadoUpdateDTO pedidoEstadoUpdateDTO) {

        if (!id.equals(pedidoEstadoUpdateDTO.getIdPedido())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        PedidoDTO updatedPedido = pedidoService.actualizarEstadoPedido(pedidoEstadoUpdateDTO);
        return ResponseEntity.ok(updatedPedido);
    }

    @GetMapping("/listarPedidos")
    public ResponseEntity<List<PedidoDTO>> listarPedidos(@RequestParam(required = false) String estado) {
        List<PedidoDTO> pedidos = pedidoService.listarPedidos(estado);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/listarPedidosAtencion")
    public ResponseEntity<List<PedidoDTO>> listarTodosLosPedidos() {
        List<PedidoDTO> pedidos = pedidoService.listarTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/pedidos/porCliente/{idCliente}")
    public ResponseEntity<List<PedidoDTO>> listarPedidosPorCliente(@PathVariable Integer idCliente) {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorCliente(idCliente);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/pedidos/porUsuario/{idUsuario}")
    public ResponseEntity<List<PedidoDTO>> listarPedidosPorUsuario(@PathVariable Integer idUsuario) {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorUsuario(idUsuario);
        return ResponseEntity.ok(pedidos);
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