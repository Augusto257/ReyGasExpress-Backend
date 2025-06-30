package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PEDIDO")
    private Integer idPedido;

    @Column(name = "METODO_PAGO", nullable = false, length = 50)
    private String metodoPago;

    @Column(name = "FECHA_PEDIDO", updatable = false)
    @CreationTimestamp
    private Timestamp fechaPedido;

    @Column(name = "FECHA_ENTREGA")
    private Timestamp fechaEntrega;

    @Column(name = "DESCUENTO")
    private Float descuento = 0f;

    @Column(name = "SUBTOTAL", nullable = false)
    private Float subtotal;

    @Column(name = "IGV", nullable = false)
    private Float igv = 0.18f; // 18%

    @Column(name = "TOTAL", nullable = false)
    private Float total;

    @Column(name = "ESTADO", nullable = false)
    private String estado = "PENDIENTE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalle_Pedido> detalles;

}
