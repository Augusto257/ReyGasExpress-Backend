package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Integer idPedido;
    private String metodoPago;
    private Timestamp fechaPedido;
    private Timestamp fechaEntrega;
    private Float descuento;
    private Float subtotal;
    private Float igv;
    private Float total;
    private String estado;
    private ClienteDTO cliente;
    private UsuarioDTO usuario;
    private List<DetallePedidoDTO> detalles;
}
