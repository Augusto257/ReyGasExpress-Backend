package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaDTO {
    private Integer idMarca;
    private String nombreMarca;
    private String tipo;
    private String estadoMarca;
}
