package com.rgev2.proyectoreygasexpressv2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistritoDTO {
    private Integer idDistrito;
    private String nombreDistrito;
    private String zonaDistrito;
}