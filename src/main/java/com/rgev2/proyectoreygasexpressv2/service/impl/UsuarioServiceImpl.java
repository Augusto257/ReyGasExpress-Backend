package com.rgev2.proyectoreygasexpressv2.service.impl;

import com.rgev2.proyectoreygasexpressv2.dto.*;
import com.rgev2.proyectoreygasexpressv2.model.Clase_Defecto;
import com.rgev2.proyectoreygasexpressv2.model.Distrito;
import com.rgev2.proyectoreygasexpressv2.model.Usuario;
import com.rgev2.proyectoreygasexpressv2.repository.ClaseDefectoRepository; // Necesario para obtener la contraseña encriptada
import com.rgev2.proyectoreygasexpressv2.repository.UsuarioRepository;
import com.rgev2.proyectoreygasexpressv2.repository.DistritoRepository;
import com.rgev2.proyectoreygasexpressv2.service.UsuarioService;
// No se necesita ClaseDefectoService aquí, el repositorio es suficiente
import com.rgev2.proyectoreygasexpressv2.service.JWTUtils; // Importar JWTUtils
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager; // Importar
import org.springframework.security.authentication.BadCredentialsException; // Importar para manejar credenciales inválidas
import org.springframework.security.authentication.DisabledException; // Importar para manejar usuario inactivo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails; // Importar para el UserDetails
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final DistritoRepository distritoRepository;
    private final ClaseDefectoRepository claseDefectoRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    private UsuarioDTO mapToUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setApellidoUsuario(usuario.getApellidoUsuario());
        dto.setNumeroTelefonoUsuario(usuario.getNumeroTelefonoUsuario());
        dto.setDireccionUsuario(usuario.getDireccionUsuario());
        dto.setCorreoUsuario(usuario.getCorreoUsuario());
        dto.setEstadoUsuario(usuario.getEstadoUsuario());
        dto.setRolUsuario(usuario.getRolUsuario());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        if (usuario.getDistrito() != null) {
            DistritoDTO distritoDto = new DistritoDTO();
            distritoDto.setIdDistrito(usuario.getDistrito().getIdDistrito());
            distritoDto.setNombreDistrito(usuario.getDistrito().getNombreDistrito());
            distritoDto.setZonaDistrito(usuario.getDistrito().getZonaDistrito());
            dto.setDistrito(distritoDto);
        }
        return dto;
    }

    private UsuarioSimpleDTO mapToUsuarioSimpleDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioSimpleDTO dto = new UsuarioSimpleDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setApellidoUsuario(usuario.getApellidoUsuario());
        dto.setCorreoUsuario(usuario.getCorreoUsuario());
        dto.setRolUsuario(usuario.getRolUsuario());
        dto.setEstadoUsuario(usuario.getEstadoUsuario());
        return dto;
    }


    @Override
    public UsuarioDTO registerUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        usuarioRepository.findByCorreoUsuario(usuarioRegistroDTO.getCorreoUsuario()).ifPresent(u -> {
            throw new RuntimeException("Ya existe un usuario con el correo: " + usuarioRegistroDTO.getCorreoUsuario());
        });

        Optional<Distrito> distritoOptional = distritoRepository.findById(usuarioRegistroDTO.getIdDistrito());
        if (distritoOptional.isEmpty()) {
            throw new RuntimeException("Distrito no encontrado con ID: " + usuarioRegistroDTO.getIdDistrito());
        }
        Distrito distrito = distritoOptional.get();

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(usuarioRegistroDTO.getNombreUsuario());
        usuario.setApellidoUsuario(usuarioRegistroDTO.getApellidoUsuario());
        usuario.setNumeroTelefonoUsuario(usuarioRegistroDTO.getNumeroTelefonoUsuario());
        usuario.setDireccionUsuario(usuarioRegistroDTO.getDireccionUsuario());
        usuario.setDistrito(distrito);
        usuario.setCorreoUsuario(usuarioRegistroDTO.getCorreoUsuario());
        usuario.setEstadoUsuario("ACTIVO");
        usuario.setRolUsuario(usuarioRegistroDTO.getRolUsuario());

        Clase_Defecto contrasena = new Clase_Defecto();
        contrasena.setDescripcionContrasena(passwordEncoder.encode(usuarioRegistroDTO.getContrasena()));

        usuario.setContrasenaUsuario(contrasena);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        return mapToUsuarioDTO(savedUsuario);
    }

    @Override
    public UsuarioDTO getUsuarioById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return mapToUsuarioDTO(usuario);
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO loginRequest) {
        AuthResponseDTO response = new AuthResponseDTO();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getCorreoUsuario(), loginRequest.getContrasena())
            );

            Usuario user = usuarioRepository.findByCorreoUsuario(loginRequest.getCorreoUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + loginRequest.getCorreoUsuario()));

            if (!"ACTIVO".equalsIgnoreCase(user.getEstadoUsuario())) {
                throw new DisabledException("El usuario está inactivo. No puede iniciar sesión.");
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);

            response.setToken(jwt);
            response.setRolUsuario(user.getRolUsuario());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Login ejecutado correctamente!!");
            response.setCorreoUsuario(user.getCorreoUsuario());
            response.setIdUsuario(user.getIdUsuario());

        } catch (BadCredentialsException e) {
            response.setStatusCode(401); // Unauthorized
            response.setError("Credenciales inválidas: Correo o contraseña incorrectos.");
            response.setMessage("Fallo en el login.");
        } catch (DisabledException e) {
            response.setStatusCode(403); // Forbidden
            response.setError(e.getMessage());
            response.setMessage("Fallo en el login.");
        } catch (RuntimeException e) {
            response.setStatusCode(404);
            response.setError(e.getMessage());
            response.setMessage("Fallo en el login.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error interno del servidor: " + e.getMessage());
            response.setMessage("Fallo en el login.");
        }
        return response;
    }

    @Override
    public AuthResponseDTO refreshToken(AuthResponseDTO refreshTokenRequest) {
        AuthResponseDTO response = new AuthResponseDTO();

        try {

            String correoUsuario = jwtUtils.extractUsername(refreshTokenRequest.getRefreshToken());

            Usuario usuario = usuarioRepository.findByCorreoUsuario(correoUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el token de refresco."));

            if (jwtUtils.isTokenValid(refreshTokenRequest.getRefreshToken(), usuario)) {
                var newJwt = jwtUtils.generateToken(usuario);
                response.setStatusCode(200);
                response.setToken(newJwt);
                response.setRefreshToken(refreshTokenRequest.getRefreshToken());
                response.setExpirationTime("24Hrs");
                response.setMessage("Token refrescado correctamente!!");
                response.setCorreoUsuario(usuario.getCorreoUsuario());
                response.setIdUsuario(usuario.getIdUsuario());
            } else {
                response.setStatusCode(401);
                response.setError("Token de refresco inválido o expirado.");
                response.setMessage("Fallo al refrescar el token.");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error al refrescar el token: " + e.getMessage());
            response.setMessage("Fallo al refrescar el token.");
        }
        return response;
    }

    @Override
    public UsuarioDTO updateUsuarioEstado(Integer idUsuario, UsuarioEstadoUpdateDTO estadoUpdateDTO) {
        Usuario existingUsuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        String nuevoEstado = estadoUpdateDTO.getEstadoUsuario();

        if (nuevoEstado == null || (!"ACTIVO".equalsIgnoreCase(nuevoEstado) && !"INACTIVO".equalsIgnoreCase(nuevoEstado))) {
            throw new IllegalArgumentException("Estado de usuario inválido. Solo se permite 'ACTIVO' o 'INACTIVO'.");
        }

        existingUsuario.setEstadoUsuario(nuevoEstado.toUpperCase());

        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return mapToUsuarioDTO(updatedUsuario);
    }

    @Override
    public UsuarioDTO getMyInfo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreoUsuario(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + correo));
        return mapToUsuarioDTO(usuario);
    }

    @Override
    public List<UsuarioSimpleDTO> getAllUsuariosExceptDueno() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> !"DUENO".equalsIgnoreCase(usuario.getRolUsuario()))
                .map(this::mapToUsuarioSimpleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioSimpleDTO> searchUsersByName(String name) {
        List<Usuario> usuarios = usuarioRepository.findByNombreUsuarioContainingIgnoreCase(name);

        return usuarios.stream()
                .map(this::mapToUsuarioSimpleDTO)
                .collect(Collectors.toList());
    }
}