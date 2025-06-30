package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Integer idProducto;

    @Column(name = "TIPO_PRODUCTO", length = 50, nullable = false)
    private String tipoProducto;

    @Column(name = "PRECIO_UNITARIO_PRODUCTO", nullable = false)
    private Float precioUnitarioProducto;

    @Column(name = "STOCK_PRODUCTO", nullable = false)
    private Integer stockProducto;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp fechaCreacion;

    @Column(name = "estado_Producto", nullable = false, length = 50)
    private String estadoProducto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MARCA", nullable = false)
    private Marca marca;

    @OneToMany(mappedBy = "producto")
    private List<Detalle_Pedido> detallePedidos;
}
