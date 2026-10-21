package com.acda.nexoreservas.web.form;

import com.acda.nexoreservas.model.entity.EstadoReserva;
import com.acda.nexoreservas.model.entity.Reserva;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// El formulario recibe identificadores de sala y cliente desde HTML.
// La entidad Reserva, en cambio, guarda referencias a ambos objetos JPA.
public class ReservaForm {

    private Long id;

    @NotNull(message = "Selecciona una sala")
    private Long salaId;

    @NotNull(message = "Selecciona un cliente")
    private Long clienteId;

    @NotNull(message = "Indica el inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime inicio;

    @NotNull(message = "Indica el fin")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fin;

    @NotNull
    @Min(value = 1, message = "Debe haber al menos un asistente")
    private Integer asistentes;

    @NotNull
    private EstadoReserva estado = EstadoReserva.CONFIRMADA;

    @Size(max = 1000)
    private String observaciones;

    // Convierte los objetos relacionados en IDs para rellenar el formulario de edición.
    public static ReservaForm desde(Reserva reserva) {
        ReservaForm form = new ReservaForm();
        form.id = reserva.getId();
        form.salaId = reserva.getSala().getId();
        form.clienteId = reserva.getCliente().getId();
        form.inicio = reserva.getInicio();
        form.fin = reserva.getFin();
        form.asistentes = reserva.getAsistentes();
        form.estado = reserva.getEstado();
        form.observaciones = reserva.getObservaciones();
        return form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalaId() {
        return salaId;
    }

    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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
