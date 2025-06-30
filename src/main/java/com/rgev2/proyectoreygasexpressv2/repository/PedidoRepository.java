package com.rgev2.proyectoreygasexpressv2.repository;

import com.rgev2.proyectoreygasexpressv2.dto.ProductSalesDTO;
import com.rgev2.proyectoreygasexpressv2.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByEstado(String estado);

    List<Pedido> findByCliente_IdCliente(Integer idCliente);

    List<Pedido> findByUsuario_IdUsuario(Integer idUsuario);

    @Query("SELECT c.distrito.nombreDistrito, pm.nombreMarca, SUM(dp.cantidad) " +
            "FROM Pedido p " +
            "JOIN p.cliente c " +
            "JOIN p.detalles dp " +
            "JOIN dp.producto pr " +
            "JOIN pr.marca pm " +
            "WHERE p.estado = 'COMPLETADO' " +
            "GROUP BY c.distrito.nombreDistrito, pm.nombreMarca " +
            "ORDER BY c.distrito.nombreDistrito, SUM(dp.cantidad) DESC")
    List<Object[]> findBrandPreferencesByDistrict();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = 'PENDIENTE'")
    Long countPendingOrders();

    @Query("SELECT SUM(dp.cantidad * dp.precioUnitario) FROM Pedido p JOIN p.detalles dp WHERE p.estado = 'COMPLETADO' AND EXTRACT(MONTH FROM p.fechaPedido) = :month AND EXTRACT(YEAR FROM p.fechaPedido) = :year")
    Double sumCompletedSalesByMonthAndYear(int month, int year);

    @Query("SELECT new com.rgev2.proyectoreygasexpressv2.dto.ProductSalesDTO(pr.idProducto, (pr.tipoProducto || ' - ' || m.nombreMarca), SUM(dp.cantidad), CAST(SUM(dp.cantidad * dp.precioUnitario) AS double)) " +
            "FROM Pedido p " +
            "JOIN p.detalles dp " +
            "JOIN dp.producto pr " +
            "JOIN pr.marca m " +
            "WHERE p.estado = 'COMPLETADO' " +
            "GROUP BY pr.idProducto, pr.tipoProducto, m.nombreMarca " +
            "ORDER BY SUM(dp.cantidad) DESC")
    List<ProductSalesDTO> findTopSellingProducts();
}
