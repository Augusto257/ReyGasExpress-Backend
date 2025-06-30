package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private Integer idDetalle;
    private Integer idProducto;
    private ProductoDTO producto;
    private Integer cantidad;
    private Float precioUnitario;
    private Float subtotal;
}
