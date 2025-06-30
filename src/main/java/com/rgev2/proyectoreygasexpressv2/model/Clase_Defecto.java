package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Tabla_Defecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clase_Defecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Contrasena")
    private Integer idContrasena;

    @Column(name = "descripcion_Contrasena", nullable = false, length = 255)
    private String descripcionContrasena;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Usuario", referencedColumnName = "id_Usuario", unique = true, nullable = false)

    private Usuario usuario;
}