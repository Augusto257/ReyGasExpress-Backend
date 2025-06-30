package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.PedidoDTO;
import com.rgev2.proyectoreygasexpressv2.dto.PedidoEstadoUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoService {


    PedidoDTO registrarPedido(PedidoDTO pedidoDTO, Integer idUsuario);

    Optional<PedidoDTO> obtenerPedidoPorId(Integer idPedido);

    PedidoDTO actualizarEstadoPedido(PedidoEstadoUpdateDTO pedidoEstadoUpdateDTO);

    List<PedidoDTO> listarPedidos(String estado);

    List<PedidoDTO> listarTodosLosPedidos();

    List<PedidoDTO> listarPedidosPorCliente(Integer idCliente);

    List<PedidoDTO> listarPedidosPorUsuario(Integer idUsuario);
}