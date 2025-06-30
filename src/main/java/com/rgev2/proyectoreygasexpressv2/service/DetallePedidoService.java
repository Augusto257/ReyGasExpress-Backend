package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.DetallePedidoDTO;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {

    Optional<DetallePedidoDTO> obtenerDetallePedidoPorId(Integer idDetalle);

    List<DetallePedidoDTO> obtenerDetallesPorPedido(Integer idPedido);

}