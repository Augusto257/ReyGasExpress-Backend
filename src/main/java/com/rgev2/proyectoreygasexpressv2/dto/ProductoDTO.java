package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Integer idProducto;
    private String tipoProducto;
    private Float precioUnitarioProducto;
    private Integer stockProducto;
    private Timestamp fechaCreacion;
    private String estadoProducto;
    private MarcaDTO marca;
}
