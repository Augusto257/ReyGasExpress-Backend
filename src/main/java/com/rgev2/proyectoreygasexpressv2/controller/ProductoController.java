package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.ProductoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductoEstadoUpdateDTO;
import com.rgev2.proyectoreygasexpressv2.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping("/dueno/registerProducto")
    public ResponseEntity<ProductoDTO> registrarProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO nuevoProducto = productoService.registrarProducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping("/productosActivos")
    public ResponseEntity<List<ProductoDTO>> listarProductosActivos() {
        List<ProductoDTO> productos = productoService.listarTodosLosProductosActivos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping ("/dueno/productos")
    public ResponseEntity<List<ProductoDTO>> listarProductos(@RequestParam(required = false) String estado) {
        List<ProductoDTO> productos = productoService.listarTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Integer id) {
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Integer id, @Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(productoActualizado);
    }

    @PutMapping("/dueno/productos/{id}/estado")
    public ResponseEntity<ProductoDTO> actualizarEstadoProducto(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoEstadoUpdateDTO estadoUpdateDTO) {
        ProductoDTO updatedProducto = productoService.actualizarEstadoProducto(id, estadoUpdateDTO);
        return ResponseEntity.ok(updatedProducto);
    }

    @GetMapping("/buscar/productosActivos")
    public ResponseEntity<List<ProductoDTO>> buscarProductosActivosPorNombre(@RequestParam String nombre) {
        List<ProductoDTO> productos = productoService.buscarProductosActivosPorTipo(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorNombreYEstado(
            @RequestParam String nombre,
            @RequestParam(required = false) String estado) {
        List<ProductoDTO> productos = productoService.buscarProductosPorTipo(nombre, estado);
        return ResponseEntity.ok(productos);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().contains("no encontrado") || e.getMessage().contains("no existe")) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>("Error: " + e.getMessage(), status);
    }
}