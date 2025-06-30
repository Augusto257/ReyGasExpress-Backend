package com.rgev2.proyectoreygasexpressv2.service;

import com.rgev2.proyectoreygasexpressv2.dto.*;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO registerUsuario(UsuarioRegistroDTO usuarioRegistroDTO);
    UsuarioDTO getUsuarioById(Integer id);

    AuthResponseDTO login(AuthRequestDTO loginRequest);
    AuthResponseDTO refreshToken(AuthResponseDTO refreshTokenRequest);

    UsuarioDTO updateUsuarioEstado(Integer idUsuario, UsuarioEstadoUpdateDTO estadoUpdateDTO);

    UsuarioDTO getMyInfo(String correo);

    List<UsuarioSimpleDTO> getAllUsuariosExceptDueno();

    List<UsuarioSimpleDTO> searchUsersByName(String name);
}