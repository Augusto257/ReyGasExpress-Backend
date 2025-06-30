package com.rgev2.proyectoreygasexpressv2.controller;

import com.rgev2.proyectoreygasexpressv2.dto.*;
import com.rgev2.proyectoreygasexpressv2.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/auth/registerUsuario")
    public ResponseEntity<UsuarioDTO> registerUsuario(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        UsuarioDTO registeredUser = usuarioService.registerUsuario(usuarioRegistroDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/dueno/registerUsuario")
    public ResponseEntity<UsuarioDTO> registerUsuarioByDueno(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        UsuarioDTO registeredUser = usuarioService.registerUsuario(usuarioRegistroDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/dueno/usuarios/search")
    public ResponseEntity<List<UsuarioSimpleDTO>> searchUsersByName(@RequestParam(required = false) String name) {
        List<UsuarioSimpleDTO> users = usuarioService.searchUsersByName(name != null ? name : "");
        return ResponseEntity.ok(users);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO loginRequest) {
        AuthResponseDTO response = usuarioService.login(loginRequest);
        if (response.getStatusCode() == 200) {
            return ResponseEntity.ok(response);
        } else if (response.getStatusCode() == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (response.getStatusCode() == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } else if (response.getStatusCode() == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/dueno/usuarios/{id}/estado")
    public ResponseEntity<UsuarioDTO> updateUsuarioEstado(
            @PathVariable Integer id,
            @RequestBody UsuarioEstadoUpdateDTO estadoUpdateDTO) {
        UsuarioDTO updatedUser = usuarioService.updateUsuarioEstado(id, estadoUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST; // Para estados inválidos
        } else if (e.getMessage().contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>("Error: " + e.getMessage(), status);
    }

    @GetMapping("/empleados/get-profile")
    public ResponseEntity<UsuarioDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String correo = authentication.getName();

        UsuarioDTO usuarioInfo = usuarioService.getMyInfo(correo);

        return ResponseEntity.ok(usuarioInfo);
    }

    @GetMapping("/dueno/usuarios")
    public ResponseEntity<List<UsuarioSimpleDTO>> getAllUsuariosExceptDueno() {
        List<UsuarioSimpleDTO> usuarios = usuarioService.getAllUsuariosExceptDueno();
        return ResponseEntity.ok(usuarios);
    }

}