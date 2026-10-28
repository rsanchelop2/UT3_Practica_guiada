package com.acda.nexoreservas.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// Reserva asocia una sala y un cliente y aporta datos propios: fechas,
// asistentes y estado. Por eso es una entidad, no un @ManyToMany directo.
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO bloque 2: sustituir @Transient por @ManyToOne y @JoinColumn.
    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservas_sala"))
    private Sala sala;

    // TODO bloque 2: mapear la clave foránea del cliente.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservas_cliente"))
    private Cliente cliente;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime inicio;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fin;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer asistentes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoReserva estado = EstadoReserva.CONFIRMADA;

    @Size(max = 1000)
    @Column(length = 1000)
    private String observaciones;

    public Reserva() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public Integer getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(Integer asistentes) {
        this.asistentes = asistentes;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
