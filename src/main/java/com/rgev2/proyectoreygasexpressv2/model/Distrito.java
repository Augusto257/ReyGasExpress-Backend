package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "Distrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Distrito")
    private Integer idDistrito;

    @Column(name = "nombre_Distrito", nullable = false, length = 50)
    private String nombreDistrito;

    @Column(name = "zona_Distrito", length = 50)
    private String zonaDistrito;

    @OneToMany(mappedBy = "distrito")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "distrito")
    private List<Cliente> clientes;
}