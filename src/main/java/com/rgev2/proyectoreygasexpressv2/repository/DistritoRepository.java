package com.rgev2.proyectoreygasexpressv2.repository;

import com.rgev2.proyectoreygasexpressv2.model.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
    Optional<Distrito> findByNombreDistrito(String nombreDistrito);
}