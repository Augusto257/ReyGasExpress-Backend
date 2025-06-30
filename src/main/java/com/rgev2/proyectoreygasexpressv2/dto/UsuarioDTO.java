package com.rgev2.proyectoreygasexpressv2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String numeroTelefonoUsuario;
    private String direccionUsuario;
    private String correoUsuario;
    private String estadoUsuario;
    private String rolUsuario;
    private Timestamp fechaCreacion;
    private DistritoDTO distrito;
}
