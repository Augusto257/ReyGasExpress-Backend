package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Marca")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Marca")
    private Integer idMarca;

    @Column(name = "nombre_Marca", length = 50, nullable = false)
    private String nombreMarca;

    @Column(name = "estado_Marca", nullable = false, length = 50)
    private String estadoMarca;

    @Column(name = "tipo_Marca", nullable = false, length = 50)
    private String tipoMarca;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Producto> productos;

}
