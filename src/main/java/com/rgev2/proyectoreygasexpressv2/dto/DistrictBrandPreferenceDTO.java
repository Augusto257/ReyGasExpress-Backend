package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictBrandPreferenceDTO {
    private String nombreDistrito;
    private List<BrandCountDTO> marcasPreferidas;
}
