package com.rgev2.proyectoreygasexpressv2.repository;

import com.rgev2.proyectoreygasexpressv2.model.Clase_Defecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaseDefectoRepository extends JpaRepository<Clase_Defecto, Integer> {
    Optional<Clase_Defecto> findByUsuarioIdUsuario(Integer idUsuario);
}