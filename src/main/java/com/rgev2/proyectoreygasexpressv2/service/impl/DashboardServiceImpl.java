// src/main/java/com/rgev2/proyectoreygasexpressv2/service/impl/DashboardServiceImpl.java
package com.rgev2.proyectoreygasexpressv2.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rgev2.proyectoreygasexpressv2.repository.ClienteRepository;
import com.rgev2.proyectoreygasexpressv2.repository.PedidoRepository;
import com.rgev2.proyectoreygasexpressv2.dto.BrandCountDTO;
import com.rgev2.proyectoreygasexpressv2.dto.DashboardSummaryDTO;
import com.rgev2.proyectoreygasexpressv2.dto.DistrictBrandPreferenceDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MonthlySalesDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductSalesDTO;
import com.rgev2.proyectoreygasexpressv2.service.DashboardService;
import java.time.LocalDate;
import java.time.Month; // Para obtener el nombre del mes
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public DashboardSummaryDTO getOverallDashboardSummary() {
        Long totalClientes = clienteRepository.countAllClients();
        Long totalPedidosPendientes = pedidoRepository.countPendingOrders();

        LocalDate now = LocalDate.now();
        Double totalVentasMesActual = pedidoRepository.sumCompletedSalesByMonthAndYear(now.getMonthValue(), now.getYear());

        DashboardSummaryDTO summary = new DashboardSummaryDTO();
        summary.setTotalClientes(totalClientes != null ? totalClientes : 0L);
        summary.setTotalPedidosPendientes(totalPedidosPendientes != null ? totalPedidosPendientes : 0L);
        summary.setTotalVentasMesActual(totalVentasMesActual != null ? totalVentasMesActual : 0.0);
        return summary;
    }

    @Override
    public List<MonthlySalesDTO> getMonthlySalesData() {
        List<MonthlySalesDTO> salesData = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 6; i++) { // Últimos 6 meses
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();
            Double totalVentas = pedidoRepository.sumCompletedSalesByMonthAndYear(month, year);
            salesData.add(new MonthlySalesDTO(Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("es", "ES")), totalVentas != null ? totalVentas : 0.0));
        }
        java.util.Collections.reverse(salesData);
        return salesData;
    }

    @Override
    public List<ProductSalesDTO> getTopSellingProducts(int limit) {
        return pedidoRepository.findTopSellingProducts().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictBrandPreferenceDTO> getClientBrandPreferencesByDistrict() {
        List<Object[]> rawData = pedidoRepository.findBrandPreferencesByDistrict();

        Map<String, List<BrandCountDTO>> districtMap = new LinkedHashMap<>();

        for (Object[] row : rawData) {
            String nombreDistrito = (String) row[0];
            String nombreMarca = (String) row[1];
            Long cantidad = ((Number) row[2]).longValue();

            districtMap.computeIfAbsent(nombreDistrito, k -> new ArrayList<>())
                    .add(new BrandCountDTO(nombreMarca, cantidad));
        }

        List<DistrictBrandPreferenceDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<BrandCountDTO>> entry : districtMap.entrySet()) {
            entry.getValue().sort(Comparator.comparing(BrandCountDTO::getCantidadPedidos).reversed());
            result.add(new DistrictBrandPreferenceDTO(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
