package com.rgev2.proyectoreygasexpressv2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Usuario")
    private Integer idUsuario;

    @Column(name = "nombre_Usuario", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "apellido_Usuario", nullable = false, length = 50)
    private String apellidoUsuario;

    @Column(name = "numeroTelefono_Usuario", nullable = false, length = 20)
    private String numeroTelefonoUsuario;

    @Column(name = "direccion_Usuario", nullable = false, length = 200)
    private String direccionUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_Distrito", referencedColumnName = "id_Distrito", nullable = false)
    private Distrito distrito;

    @Column(name = "correo_Usuario", unique = true, nullable = false, length = 50)
    private String correoUsuario;

    @Column(name = "estado_Usuario", nullable = false, length = 50)
    private String estadoUsuario;

    @Column(name = "rol_Usuario", nullable = false, length = 50)
    private String rolUsuario;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp fechaCreacion;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Clase_Defecto contrasenaUsuario;

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rolUsuario));
    }

    @Override
    public String getPassword() {
        return (this.contrasenaUsuario != null) ? this.contrasenaUsuario.getDescripcionContrasena() : null;
    }

    @Override
    public String getUsername() {
        return this.correoUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVO".equalsIgnoreCase(this.estadoUsuario);
    }

    public void setContrasenaUsuario(Clase_Defecto contrasenaUsuario) {
        if (contrasenaUsuario != null) {
            contrasenaUsuario.setUsuario(this);
        }
        this.contrasenaUsuario = contrasenaUsuario;
    }
}
