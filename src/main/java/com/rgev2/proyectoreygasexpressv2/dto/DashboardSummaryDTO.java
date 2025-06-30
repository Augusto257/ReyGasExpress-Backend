package com.rgev2.proyectoreygasexpressv2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalClientes;
    private Long totalPedidosPendientes;
    private Double totalVentasMesActual;
}