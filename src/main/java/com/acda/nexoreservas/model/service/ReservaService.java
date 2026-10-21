package com.acda.nexoreservas.model.service;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.exception.ResourceNotFoundException;
import com.acda.nexoreservas.model.entity.EstadoReserva;
import com.acda.nexoreservas.model.entity.Reserva;
import com.acda.nexoreservas.model.repository.ReservaRepository;
import com.acda.nexoreservas.web.form.ReservaForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ReservaService(ReservaRepository reservas) {
        this.reservas = reservas;
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
        // TODO bloque 2: recuperar sala y cliente, validar fechas, capacidad,
        // estado y solapamiento; crear o modificar y guardar la asociación.
        throw new BusinessException("Guardar reserva: operación pendiente de la práctica");
    }

    @Transactional
    public void eliminar(Long id) {
        // TODO bloque 2: comprobar si ya comenzó y eliminar.
        throw new BusinessException("Eliminar reserva: operación pendiente de la práctica");
    }
}
