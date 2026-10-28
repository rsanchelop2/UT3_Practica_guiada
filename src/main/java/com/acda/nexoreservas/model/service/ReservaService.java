package com.acda.nexoreservas.model.service;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.exception.ResourceNotFoundException;
import com.acda.nexoreservas.model.entity.Cliente;
import com.acda.nexoreservas.model.entity.EstadoReserva;
import com.acda.nexoreservas.model.entity.Reserva;
import com.acda.nexoreservas.model.entity.Sala;
import com.acda.nexoreservas.model.repository.ClienteRepository;
import com.acda.nexoreservas.model.repository.ReservaRepository;
import com.acda.nexoreservas.model.repository.SalaRepository;
import com.acda.nexoreservas.web.form.ReservaForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {
    // TODO bloque 4: traducir estas claves a propiedades para Sort.
    private static final Map<String, String> ORDENES = Map.of(
            "inicio", "inicio", "fin", "fin", "estado", "estado",
            "asistentes", "asistentes", "sala", "sala.nombre",
            "cliente", "cliente.apellidos");

    private final ReservaRepository reservas;
    private final SalaRepository salas;
    private final ClienteRepository clientes;

    public ReservaService(ReservaRepository reservas, SalaRepository salas, ClienteRepository clientes) {
        this.reservas = reservas;
        this.salas = salas;
        this.clientes = clientes;
    }

    @Transactional(readOnly = true)
    public List<Reserva> buscar(String q, EstadoReserva estado, LocalDateTime desde,
                                LocalDateTime hasta, Long salaId, Long clienteId,
                                String orden, String direccion) {
        // TODO bloque 3: filtros de estado, fechas, texto y relaciones.
        // TODO bloque 4: ordenar con Sort y ORDENES tras mapear las relaciones.
        return reservas.findAll();
    }

    @Transactional(readOnly = true)
    public Reserva obtener(Long id) {
        return reservas.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La reserva solicitada no existe"));
    }

    @Transactional(readOnly = true)
    public long totalFuturasConfirmadas() {
        return reservas.findAll().stream()
                .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA
                        && r.getInicio().isAfter(LocalDateTime.now())).count();
    }

    @Transactional(readOnly = true)
    public List<Reserva> proximas() {
        return reservas.findAll().stream()
                .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA
                        && r.getInicio().isAfter(LocalDateTime.now()))
                .sorted((a, b) -> a.getInicio().compareTo(b.getInicio()))
                .limit(5).toList();
    }

    @Transactional
    public Reserva guardar(ReservaForm form) {
        if (form == null || form.getSalaId() == null || form.getClienteId() == null) {
            throw new BusinessException("Selecciona una sala y un cliente para la reserva");
        }
        if (form.getInicio() == null || form.getFin() == null
                || !form.getInicio().isBefore(form.getFin())) {
            throw new BusinessException("El inicio debe ser anterior al fin");
        }
        LocalDateTime ahora = LocalDateTime.now();
        if (!form.getInicio().isAfter(ahora)) {
            throw new BusinessException("El inicio de la reserva debe ser posterior a la hora actual");
        }
        if (Duration.between(form.getInicio(), form.getFin()).toMinutes() < 30) {
            throw new BusinessException("La reserva debe durar al menos 30 minutos");
        }
        if (form.getAsistentes() == null || form.getAsistentes() < 1) {
            throw new BusinessException("El número de asistentes debe ser mayor que cero");
        }
        if (form.getEstado() == null) {
            throw new BusinessException("El estado de la reserva es obligatorio");
        }

        Reserva reserva = form.getId() == null ? new Reserva() : obtener(form.getId());
        if (form.getId() != null && !reserva.getInicio().isAfter(ahora)) {
            throw new BusinessException("No se puede modificar una reserva cuyo inicio ya ha llegado");
        }

        Sala sala = salas.findById(form.getSalaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala inexistente"));
        Cliente cliente = clientes.findById(form.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente inexistente"));

        if (form.getAsistentes() > sala.getCapacidad()) {
            throw new BusinessException("El número de asistentes supera la capacidad de la sala");
        }

        if (form.getEstado() == EstadoReserva.CONFIRMADA
                && (!Boolean.TRUE.equals(sala.getActiva()) || !Boolean.TRUE.equals(cliente.getActivo()))) {
            throw new BusinessException("Para confirmar la reserva, la sala y el cliente deben estar activos");
        }

        reserva.setSala(sala);
        reserva.setCliente(cliente);
        reserva.setInicio(form.getInicio());
        reserva.setFin(form.getFin());
        reserva.setAsistentes(form.getAsistentes());
        reserva.setEstado(form.getEstado());
        reserva.setObservaciones(form.getObservaciones());

        return reservas.saveAndFlush(reserva);
    }

    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = obtener(id);
        if (!reserva.getInicio().isAfter(LocalDateTime.now())) {
            throw new BusinessException("No se puede eliminar una reserva cuyo inicio ya ha llegado");
        }
        reservas.delete(reserva);
        reservas.flush();
    }
}
