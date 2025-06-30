package com.rgev2.proyectoreygasexpressv2.repository;

import com.rgev2.proyectoreygasexpressv2.model.Marca;
import com.rgev2.proyectoreygasexpressv2.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByEstadoProductoIgnoreCase(String estadoProducto);
    List<Producto> findByTipoProductoContainingIgnoreCase(String tipoProducto);
    List<Producto> findByTipoProductoContainingIgnoreCaseAndEstadoProductoIgnoreCase(String tipoProducto, String estadoProducto);
    Optional<Producto> findByTipoProductoIgnoreCaseAndMarca(String tipoProducto, Marca marca);
}
