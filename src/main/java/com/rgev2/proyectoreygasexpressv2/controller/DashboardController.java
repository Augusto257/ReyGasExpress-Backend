package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.DashboardSummaryDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MonthlySalesDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductSalesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rgev2.proyectoreygasexpressv2.service.DashboardService;
import com.rgev2.proyectoreygasexpressv2.dto.DistrictBrandPreferenceDTO; // NUEVO
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        DashboardSummaryDTO summary = dashboardService.getOverallDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<List<MonthlySalesDTO>> getMonthlySales() {
        List<MonthlySalesDTO> sales = dashboardService.getMonthlySalesData();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<List<ProductSalesDTO>> getTopSellingProducts() {
        // Puedes pasar un límite aquí o dejar que el servicio decida el default
        List<ProductSalesDTO> topProducts = dashboardService.getTopSellingProducts(5);
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/client-brand-preferences")
    public ResponseEntity<List<DistrictBrandPreferenceDTO>> getClientBrandPreferences() {
        List<DistrictBrandPreferenceDTO> preferences = dashboardService.getClientBrandPreferencesByDistrict();
        return ResponseEntity.ok(preferences);
    }
}
