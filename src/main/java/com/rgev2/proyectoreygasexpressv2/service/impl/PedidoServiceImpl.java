package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.*;
import com.rgev2.proyectoreygasexpressv2.model.*;
import com.rgev2.proyectoreygasexpressv2.repository.*;
import com.rgev2.proyectoreygasexpressv2.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    private PedidoDTO mapToPedidoDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setFechaEntrega(pedido.getFechaEntrega());
        dto.setDescuento(pedido.getDescuento());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setIgv(pedido.getIgv());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());

        if (pedido.getCliente() != null) {
            ClienteDTO clienteDto = new ClienteDTO();
            clienteDto.setIdCliente(pedido.getCliente().getIdCliente());
            clienteDto.setNombreCliente(pedido.getCliente().getNombreCliente());
            clienteDto.setApellidoCliente(pedido.getCliente().getApellidoCliente());
            clienteDto.setDireccionCliente(pedido.getCliente().getDireccionCliente());

            if (pedido.getCliente().getDistrito() != null) {
                DistritoDTO distritoDto = new DistritoDTO();
                distritoDto.setIdDistrito(pedido.getCliente().getDistrito().getIdDistrito());
                distritoDto.setNombreDistrito(pedido.getCliente().getDistrito().getNombreDistrito());
                clienteDto.setDistrito(distritoDto);
            }
            dto.setCliente(clienteDto);
        }

        if (pedido.getUsuario() != null) {
            UsuarioDTO usuarioDto = new UsuarioDTO();
            usuarioDto.setIdUsuario(pedido.getUsuario().getIdUsuario());
            usuarioDto.setNombreUsuario(pedido.getUsuario().getNombreUsuario());
            usuarioDto.setApellidoUsuario(pedido.getUsuario().getApellidoUsuario());
            usuarioDto.setCorreoUsuario(pedido.getUsuario().getCorreoUsuario());
            dto.setUsuario(usuarioDto);
        }

        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            dto.setDetalles(pedido.getDetalles().stream()
                    .map(this::mapToDetallePedidoDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private DetallePedidoDTO mapToDetallePedidoDTO(Detalle_Pedido detallePedido) {
        if (detallePedido == null) {
            return null;
        }
        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setIdDetalle(detallePedido.getIdDetalle());
        dto.setCantidad(detallePedido.getCantidad());
        dto.setPrecioUnitario(detallePedido.getPrecioUnitario());
        dto.setSubtotal(detallePedido.getSubtotal());

        if (detallePedido.getProducto() != null) {
            ProductoDTO productoDto = new ProductoDTO();
            productoDto.setIdProducto(detallePedido.getProducto().getIdProducto());
            productoDto.setTipoProducto(detallePedido.getProducto().getTipoProducto());
            productoDto.setPrecioUnitarioProducto(detallePedido.getProducto().getPrecioUnitarioProducto());
            productoDto.setStockProducto(detallePedido.getProducto().getStockProducto());
            productoDto.setEstadoProducto(detallePedido.getProducto().getEstadoProducto());

            if (detallePedido.getProducto().getMarca() != null) {
                MarcaDTO marcaDto = new MarcaDTO();
                marcaDto.setIdMarca(detallePedido.getProducto().getMarca().getIdMarca());
                marcaDto.setNombreMarca(detallePedido.getProducto().getMarca().getNombreMarca());
                marcaDto.setTipo(detallePedido.getProducto().getMarca().getTipoMarca());
                marcaDto.setEstadoMarca(detallePedido.getProducto().getMarca().getEstadoMarca());
                productoDto.setMarca(marcaDto);
            }
            dto.setProducto(productoDto);
        }
        return dto;
    }

    @Override
    @Transactional
    public PedidoDTO registrarPedido(PedidoDTO pedidoDTO, Integer idUsuario) {
        Cliente cliente = clienteRepository.findById(pedidoDTO.getCliente().getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + pedidoDTO.getCliente().getIdCliente()));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario (empleado) no encontrado con ID: " + idUsuario));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUsuario(usuario);
        pedido.setMetodoPago(pedidoDTO.getMetodoPago());
        pedido.setDescuento(pedidoDTO.getDescuento() != null ? pedidoDTO.getDescuento() : 0f);

        pedido.setEstado(pedidoDTO.getEstado() != null && !pedidoDTO.getEstado().trim().isEmpty() ? pedidoDTO.getEstado().toUpperCase() : "PENDIENTE");
        pedido.setFechaPedido(Timestamp.valueOf(LocalDateTime.now()));

        Float subtotalCalculado = 0f;
        List<Detalle_Pedido> detallesParaGuardar = new java.util.ArrayList<>();

        if (pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto.");
        }

        for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + detalleDTO.getIdProducto()));

            String productoDescriptivo = producto.getTipoProducto() + " " +
                    (producto.getMarca() != null ? producto.getMarca().getNombreMarca() : "Marca Desconocida");


            if (detalleDTO.getCantidad() == null || detalleDTO.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad para el producto '" + productoDescriptivo + "' (ID: " + producto.getIdProducto() + ") debe ser mayor a cero.");
            }

            if (producto.getStockProducto() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + productoDescriptivo + ". Stock disponible: " + producto.getStockProducto());
            }
            if ("INACTIVO".equalsIgnoreCase(producto.getEstadoProducto())) {
                throw new IllegalArgumentException("El producto '" + productoDescriptivo + "' está inactivo y no puede ser incluido en un pedido.");
            }
            if (producto.getMarca() == null || "INACTIVO".equalsIgnoreCase(producto.getMarca().getEstadoMarca())) {
                throw new IllegalArgumentException("La marca del producto '" + productoDescriptivo + "' está inactiva o no existe y no puede ser incluida en un pedido.");
            }

            Detalle_Pedido detalle = new Detalle_Pedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());

            detalle.setPrecioUnitario(producto.getPrecioUnitarioProducto());
            detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());

            subtotalCalculado += detalle.getSubtotal();

            producto.setStockProducto(producto.getStockProducto() - detalle.getCantidad());
            productoRepository.save(producto);

            detallesParaGuardar.add(detalle);
        }

        pedido.setSubtotal(subtotalCalculado);
        Float descuentoMonto = pedido.getSubtotal() * (pedido.getDescuento() / 100f);
        Float subtotalConDescuentoAplicado = pedido.getSubtotal() - descuentoMonto;
        if (subtotalConDescuentoAplicado < 0) subtotalConDescuentoAplicado = 0f;

        pedido.setIgv(subtotalConDescuentoAplicado * 0.18f);
        pedido.setTotal(subtotalConDescuentoAplicado + pedido.getIgv());

        pedido.setDetalles(detallesParaGuardar);

        Pedido savedPedido = pedidoRepository.save(pedido);

        return mapToPedidoDTO(savedPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoDTO> obtenerPedidoPorId(Integer idPedido) {
        return pedidoRepository.findById(idPedido).map(this::mapToPedidoDTO);
    }

    @Override
    @Transactional
    public PedidoDTO actualizarEstadoPedido(PedidoEstadoUpdateDTO pedidoEstadoUpdateDTO) {
        if (pedidoEstadoUpdateDTO.getIdPedido() == null) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio para actualizar el estado.");
        }
        if (pedidoEstadoUpdateDTO.getEstado() == null || pedidoEstadoUpdateDTO.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del pedido no puede ser nulo o vacío.");
        }

        Pedido existingPedido = pedidoRepository.findById(pedidoEstadoUpdateDTO.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoEstadoUpdateDTO.getIdPedido()));

        String nuevoEstado = pedidoEstadoUpdateDTO.getEstado().toUpperCase();

        if (!("PENDIENTE".equals(nuevoEstado) || "COMPLETADO".equals(nuevoEstado) || "CANCELADO".equals(nuevoEstado))) {
            throw new IllegalArgumentException("Estado de pedido inválido: " + nuevoEstado);
        }

        existingPedido.setEstado(nuevoEstado);

        if ("COMPLETADO".equals(nuevoEstado) && existingPedido.getFechaEntrega() == null) {
            existingPedido.setFechaEntrega(Timestamp.valueOf(LocalDateTime.now()));
        }

        Pedido updatedPedido = pedidoRepository.save(existingPedido);
        return mapToPedidoDTO(updatedPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidos(String estado) {
        List<Pedido> pedidos;
        if (estado != null && !estado.trim().isEmpty()) {

            pedidos = pedidoRepository.findByEstado(estado.toUpperCase());
        } else {
            pedidos = pedidoRepository.findAll();
        }
        return pedidos.stream()
                .map(this::mapToPedidoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToPedidoDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorCliente(Integer idCliente) {
        return pedidoRepository.findByCliente_IdCliente(idCliente).stream()
                .map(this::mapToPedidoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuario_IdUsuario(idUsuario).stream()
                .map(this::mapToPedidoDTO)
                .collect(Collectors.toList());
    }
}