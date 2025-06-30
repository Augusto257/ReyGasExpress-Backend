package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.DetallePedidoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MarcaDTO;
import com.rgev2.proyectoreygasexpressv2.model.Detalle_Pedido;
import com.rgev2.proyectoreygasexpressv2.repository.DetallePedidoRepository;
import com.rgev2.proyectoreygasexpressv2.service.DetallePedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

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
            productoDto.setEstadoProducto(detallePedido.getProducto().getEstadoProducto());
            productoDto.setStockProducto(detallePedido.getProducto().getStockProducto());

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
    @Transactional(readOnly = true)
    public Optional<DetallePedidoDTO> obtenerDetallePedidoPorId(Integer idDetalle) {
        return detallePedidoRepository.findById(idDetalle).map(this::mapToDetallePedidoDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoDTO> obtenerDetallesPorPedido(Integer idPedido) {
        return detallePedidoRepository.findByPedido_IdPedido(idPedido).stream()
                .map(this::mapToDetallePedidoDTO)
                .collect(Collectors.toList());
    }
}