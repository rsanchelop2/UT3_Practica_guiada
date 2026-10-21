package com.acda.nexoreservas.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.*;

// El email mantiene una restricción única en la base de datos.
// El bloque 1 añadirá una comprobación previa en el servicio.
@Entity
@Table(name = "clientes", uniqueConstraints = @UniqueConstraint(name = "uk_clientes_email", columnNames = "email"))
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String nombre;

    // TODO bloque 1: añadir las restricciones de validación y columna como en Sala.
    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 160)
    @Column(nullable = false, length = 160)
    // TODO bloque 1: precisar longitud y nulabilidad de la columna.
    private String email;

    // TODO bloque 1: validar el formato opcional y limitar la columna.
    @Size(max = 20)
    @Pattern(
            regexp = "^\\+?[0-9\\s()\\-]+$",
            message = "El formato del teléfono no es válido. Solo se permiten números, espacios, paréntesis, guiones y un '+' opcional al inicio."
    )
    private String telefono;

    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
