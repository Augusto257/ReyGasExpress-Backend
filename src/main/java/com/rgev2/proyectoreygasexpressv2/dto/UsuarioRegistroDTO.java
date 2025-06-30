package com.rgev2.proyectoreygasexpressv2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidoUsuario;

    @NotBlank(message = "El número de teléfono es obligatorio")
    private String numeroTelefonoUsuario;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccionUsuario;

    @NotNull(message = "El ID del distrito es obligatorio")
    private Integer idDistrito;

    @NotBlank(message = "El estado por defecto es 'ACTIVO'")
    private String estadoUsuario;

    @NotBlank(message = "El rol de usuario es obligatorio")
    private String rolUsuario;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo es inválido")
    private String correoUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}