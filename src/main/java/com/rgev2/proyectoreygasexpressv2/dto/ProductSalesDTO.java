package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesDTO {
    private Integer idProducto;
    private String nombreProducto;
    private Long cantidadVendida;
    private Double ingresosGenerados;
}
