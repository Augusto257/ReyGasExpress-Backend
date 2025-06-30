package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.DistrictBrandPreferenceDTO; // NUEVO
import java.util.List;

import com.rgev2.proyectoreygasexpressv2.dto.DashboardSummaryDTO;
import com.rgev2.proyectoreygasexpressv2.dto.MonthlySalesDTO;
import com.rgev2.proyectoreygasexpressv2.dto.ProductSalesDTO;
import com.rgev2.proyectoreygasexpressv2.dto.DistrictBrandPreferenceDTO;
import java.util.List;

public interface DashboardService {
    DashboardSummaryDTO getOverallDashboardSummary();
    List<MonthlySalesDTO> getMonthlySalesData();
    List<ProductSalesDTO> getTopSellingProducts(int limit);
    List<DistrictBrandPreferenceDTO> getClientBrandPreferencesByDistrict();
}
