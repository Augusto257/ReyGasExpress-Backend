package com.rgev2.proyectoreygasexpressv2.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Integer idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String direccionCliente;
    private Timestamp fechaCreacion;
    private DistritoDTO distrito;
}
