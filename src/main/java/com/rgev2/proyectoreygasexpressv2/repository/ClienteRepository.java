package com.rgev2.proyectoreygasexpressv2.repository;

import com.rgev2.proyectoreygasexpressv2.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByNombreClienteAndApellidoClienteAndDireccionCliente(String nombre, String apellido, String direccion);

    @Query("SELECT COUNT(c) FROM Cliente c")
    Long countAllClients();
}