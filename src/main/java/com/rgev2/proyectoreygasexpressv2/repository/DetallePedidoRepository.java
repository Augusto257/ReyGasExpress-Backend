package com.rgev2.proyectoreygasexpressv2.repository;

import com.rgev2.proyectoreygasexpressv2.model.Detalle_Pedido;
import com.rgev2.proyectoreygasexpressv2.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetallePedidoRepository extends JpaRepository<Detalle_Pedido, Integer> {

    List<Detalle_Pedido> findByPedido(Pedido pedido);

    List<Detalle_Pedido> findByPedido_IdPedido(Integer idPedido);
}