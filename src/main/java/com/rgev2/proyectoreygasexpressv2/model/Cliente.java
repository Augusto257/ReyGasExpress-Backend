package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Cliente")
    private Integer idCliente;

    @Column(name = "nombre_Cliente", nullable = false, length = 50)
    private String nombreCliente;

    @Column(name = "apellido_Cliente", nullable = false, length = 50)
    private String apellidoCliente;

    @Column(name = "direccion_Cliente", nullable = false, length = 200)
    private String direccionCliente;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_Distrito", referencedColumnName = "id_Distrito", nullable = false)
    private Distrito distrito;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}
