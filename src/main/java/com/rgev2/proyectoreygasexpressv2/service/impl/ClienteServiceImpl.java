package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.ClienteDTO;
import com.rgev2.proyectoreygasexpressv2.dto.DistritoDTO;
import com.rgev2.proyectoreygasexpressv2.model.Cliente;
import com.rgev2.proyectoreygasexpressv2.model.Distrito;
import com.rgev2.proyectoreygasexpressv2.repository.ClienteRepository;
import com.rgev2.proyectoreygasexpressv2.repository.DistritoRepository;
import com.rgev2.proyectoreygasexpressv2.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final DistritoRepository distritoRepository;

    private ClienteDTO mapToClienteDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombreCliente(cliente.getNombreCliente());
        dto.setApellidoCliente(cliente.getApellidoCliente());
        dto.setDireccionCliente(cliente.getDireccionCliente());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        if (cliente.getDistrito() != null) {
            DistritoDTO distritoDto = new DistritoDTO();
            distritoDto.setIdDistrito(cliente.getDistrito().getIdDistrito());
            distritoDto.setNombreDistrito(cliente.getDistrito().getNombreDistrito());
            distritoDto.setZonaDistrito(cliente.getDistrito().getZonaDistrito());
            dto.setDistrito(distritoDto);
        }
        return dto;
    }

    @Override
    @Transactional
    public ClienteDTO registrarCliente(ClienteDTO clienteDTO) {
        if (clienteDTO.getDistrito() == null || clienteDTO.getDistrito().getIdDistrito() == null) {
            throw new IllegalArgumentException("El ID del distrito es obligatorio para registrar un cliente.");
        }

        Distrito distrito = distritoRepository.findById(clienteDTO.getDistrito().getIdDistrito())
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con ID: " + clienteDTO.getDistrito().getIdDistrito()));

        clienteRepository.findByNombreClienteAndApellidoClienteAndDireccionCliente(
                        clienteDTO.getNombreCliente(),
                        clienteDTO.getApellidoCliente(),
                        clienteDTO.getDireccionCliente())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un cliente con el mismo nombre, apellido y dirección.");
                });

        Cliente cliente = new Cliente();
        cliente.setNombreCliente(clienteDTO.getNombreCliente());
        cliente.setApellidoCliente(clienteDTO.getApellidoCliente());
        cliente.setDireccionCliente(clienteDTO.getDireccionCliente());
        cliente.setDistrito(distrito);

        Cliente savedCliente = clienteRepository.save(cliente);
        return mapToClienteDTO(savedCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorId(Integer idCliente) {
        return clienteRepository.findById(idCliente).map(this::mapToClienteDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> buscarClientePorNombreApellidoDireccion(String nombre, String apellido, String direccion) {
        return clienteRepository.findByNombreClienteAndApellidoClienteAndDireccionCliente(nombre, apellido, direccion)
                .map(this::mapToClienteDTO);
    }

    @Override
    public List<ClienteDTO> listarTodosLosClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapToClienteDTO)
                .collect(Collectors.toList());
    }
}