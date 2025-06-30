package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.MarcaDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductoEstadoUpdateDTO;
import com.rgev2.proyectoreygasexpressv2.model.Marca;
import com.rgev2.proyectoreygasexpressv2.model.Producto;
import com.rgev2.proyectoreygasexpressv2.repository.MarcaRepository;
import com.rgev2.proyectoreygasexpressv2.repository.ProductoRepository;
import com.rgev2.proyectoreygasexpressv2.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;

    private ProductoDTO mapToProductoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setTipoProducto(producto.getTipoProducto());
        dto.setPrecioUnitarioProducto(producto.getPrecioUnitarioProducto());
        dto.setStockProducto(producto.getStockProducto());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setEstadoProducto(producto.getEstadoProducto());

        if (producto.getMarca() != null) {
            MarcaDTO marcaDto = new MarcaDTO();
            marcaDto.setIdMarca(producto.getMarca().getIdMarca());
            marcaDto.setNombreMarca(producto.getMarca().getNombreMarca());
            marcaDto.setEstadoMarca(producto.getMarca().getEstadoMarca());
            dto.setMarca(marcaDto);
        }
        return dto;
    }

    @Override
    public ProductoDTO registrarProducto(ProductoDTO productoDTO) {

        if (productoDTO.getMarca() == null || productoDTO.getMarca().getIdMarca() == null) {
            throw new IllegalArgumentException("El ID de la marca es obligatorio para registrar un producto.");
        }

        Marca marca = marcaRepository.findById(productoDTO.getMarca().getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + productoDTO.getMarca().getIdMarca()));

        if ("INACTIVO".equalsIgnoreCase(marca.getEstadoMarca())) {
            throw new IllegalArgumentException("No se puede registrar un producto con una marca inactiva.");
        }

        productoRepository.findByTipoProductoIgnoreCaseAndMarca(productoDTO.getTipoProducto(), marca)
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Ya existe un producto con el nombre '" + productoDTO.getTipoProducto() + "' para la marca seleccionada.");
                });


        Producto producto = new Producto();
        producto.setTipoProducto(productoDTO.getTipoProducto());
        producto.setPrecioUnitarioProducto(productoDTO.getPrecioUnitarioProducto());
        producto.setStockProducto(productoDTO.getStockProducto());
        producto.setMarca(marca);
        producto.setEstadoProducto(productoDTO.getEstadoProducto() != null ? productoDTO.getEstadoProducto().toUpperCase() : "ACTIVO");


        Producto productoGuardado = productoRepository.save(producto);
        return mapToProductoDTO(productoGuardado);
    }

    @Override
    public List<ProductoDTO> listarTodosLosProductosActivos() {
        return productoRepository.findByEstadoProductoIgnoreCase("ACTIVO").stream()
                .map(this::mapToProductoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoDTO> listarTodosLosProductos() {
        return productoRepository.findAll().stream()
                .map(this::mapToProductoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return mapToProductoDTO(producto);
    }

    @Override
    public ProductoDTO actualizarProducto(Integer id, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (productoDTO.getMarca() == null || productoDTO.getMarca().getIdMarca() == null) {
            throw new IllegalArgumentException("El ID de la marca es obligatorio para actualizar un producto.");
        }

        Marca nuevaMarca = productoExistente.getMarca(); // Inicialmente, asume la misma marca
        if (!productoDTO.getMarca().getIdMarca().equals(productoExistente.getMarca().getIdMarca())) {
            nuevaMarca = marcaRepository.findById(productoDTO.getMarca().getIdMarca())
                    .orElseThrow(() -> new RuntimeException("Nueva marca no encontrada con ID: " + productoDTO.getMarca().getIdMarca()));
            // Opcional: Validar si la nueva marca está activa
            if ("INACTIVO".equalsIgnoreCase(nuevaMarca.getEstadoMarca())) {
                throw new IllegalArgumentException("No se puede asignar un producto a una marca inactiva.");
            }
        }

        // Validación para evitar duplicados por nombre de producto y marca (si el nombre o la marca cambian)
        if (!productoDTO.getTipoProducto().equalsIgnoreCase(productoExistente.getTipoProducto()) ||
                !nuevaMarca.equals(productoExistente.getMarca())) {
            productoRepository.findByTipoProductoIgnoreCaseAndMarca(productoDTO.getTipoProducto(), nuevaMarca)
                    .ifPresent(p -> {
                        if (!p.getIdProducto().equals(id)) { // Asegurarse de que no sea el mismo producto
                            throw new IllegalArgumentException("Ya existe un producto con el nombre '" + productoDTO.getTipoProducto() + "' para la marca seleccionada.");
                        }
                    });
        }


        productoExistente.setTipoProducto(productoDTO.getTipoProducto());
        productoExistente.setPrecioUnitarioProducto(productoDTO.getPrecioUnitarioProducto());
        productoExistente.setStockProducto(productoDTO.getStockProducto());
        productoExistente.setMarca(nuevaMarca); // Asignar la marca (puede ser la misma o una nueva)
        productoExistente.setEstadoProducto(productoDTO.getEstadoProducto() != null ? productoDTO.getEstadoProducto().toUpperCase() : productoExistente.getEstadoProducto());


        Producto productoActualizado = productoRepository.save(productoExistente);
        return mapToProductoDTO(productoActualizado);
    }

    @Override
    public ProductoDTO actualizarEstadoProducto(Integer idProducto, ProductoEstadoUpdateDTO estadoUpdateDTO) {
        Producto existingProducto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

        String nuevoEstado = estadoUpdateDTO.getEstadoProducto().toUpperCase();

        if (!"ACTIVO".equals(nuevoEstado) && !"INACTIVO".equals(nuevoEstado)) {
            throw new IllegalArgumentException("Estado de producto inválido. Solo se permite 'ACTIVO' o 'INACTIVO'.");
        }

        existingProducto.setEstadoProducto(nuevoEstado);
        Producto updatedProducto = productoRepository.save(existingProducto);
        return mapToProductoDTO(updatedProducto);
    }

    @Override
    public List<ProductoDTO> buscarProductosPorTipo(String nombre, String estado) {
        List<Producto> productos;
        if (estado == null || estado.equalsIgnoreCase("TODOS")) {
            productos = productoRepository.findByTipoProductoContainingIgnoreCase(nombre);
        } else if (estado.equalsIgnoreCase("ACTIVO") || estado.equalsIgnoreCase("INACTIVO")) {
            productos = productoRepository.findByTipoProductoContainingIgnoreCaseAndEstadoProductoIgnoreCase(nombre, estado);
        } else {
            throw new IllegalArgumentException("Estado de producto inválido para la búsqueda. Use 'ACTIVO', 'INACTIVO' o 'TODOS'.");
        }
        return productos.stream()
                .map(this::mapToProductoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoDTO> buscarProductosActivosPorTipo(String nombre) {
        return productoRepository.findByTipoProductoContainingIgnoreCaseAndEstadoProductoIgnoreCase(nombre, "ACTIVO").stream()
                .map(this::mapToProductoDTO)
                .collect(Collectors.toList());
    }
}