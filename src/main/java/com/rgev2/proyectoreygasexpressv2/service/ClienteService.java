package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.ClienteDTO;
import com.rgev2.proyectoreygasexpressv2.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    ClienteDTO registrarCliente(ClienteDTO clienteDTO);

    Optional<ClienteDTO> obtenerClientePorId(Integer idCliente);

    Optional<ClienteDTO> buscarClientePorNombreApellidoDireccion(String nombre, String apellido, String direccion);

    List<ClienteDTO> listarTodosLosClientes();

}