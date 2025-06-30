package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.ProductoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductoEstadoUpdateDTO;
import java.util.List;

public interface ProductoService {
    ProductoDTO registrarProducto(ProductoDTO productoDTO);

    List<ProductoDTO> listarTodosLosProductosActivos();
    List<ProductoDTO> listarTodosLosProductos();

    ProductoDTO obtenerProductoPorId(Integer id);

    ProductoDTO actualizarProducto(Integer id, ProductoDTO productoDTO);

    ProductoDTO actualizarEstadoProducto(Integer idProducto, ProductoEstadoUpdateDTO estadoUpdateDTO);

    List<ProductoDTO> buscarProductosPorTipo(String tipo, String estado);

    List<ProductoDTO> buscarProductosActivosPorTipo(String tipo);
}