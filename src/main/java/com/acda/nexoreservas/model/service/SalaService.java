package com.acda.nexoreservas.model.service;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.exception.ResourceNotFoundException;
import com.acda.nexoreservas.model.entity.Sala;
import com.acda.nexoreservas.model.repository.ReservaRepository;
import com.acda.nexoreservas.model.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class SalaService {
    // TODO bloque 4: usar solo estas propiedades al construir Sort.
    private static final Set<String> ORDENES = Set.of(
            "codigo", "nombre", "ubicacion", "capacidad", "tarifaHora", "activa");

    private final SalaRepository salas;
    private final ReservaRepository reservas;

    public SalaService(SalaRepository salas, ReservaRepository reservas) {
        this.salas = salas;
        this.reservas = reservas;
    }


    @Transactional(readOnly = true)
    public List<Sala> buscar(String q, String ubicacion, Boolean activa,
                             Integer capacidad, String orden, String direccion) {
        // TODO bloque 3: aplicar filtros con una consulta de repositorio.
        // TODO bloque 4: aplicar Sort.Direction.ASC/DESC con ORDENES.
        String texto = q == null || q.isBlank() ? null : q.trim();
        String lugar = ubicacion == null || ubicacion.isBlank()
                ? null : ubicacion.trim();
        return salas.buscar(texto, lugar, activa, capacidad);
    }

    @Transactional(readOnly = true)
    public Sala obtener(Long id) {
        return salas.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La sala solicitada no existe"));
    }

    @Transactional(readOnly = true)
    public List<Sala> todas() {
        return salas.findAll();
    }

    @Transactional(readOnly = true)
    public long total() {
        return salas.count();
    }

    @Transactional(readOnly = true)
    public long activas() {
        return salas.countByActivaTrue();
    }

    @Transactional
    public Sala guardar(Sala entrada) {
        if (entrada == null || entrada.getCodigo() == null || entrada.getCodigo().isBlank()
                || entrada.getNombre() == null || entrada.getNombre().isBlank()
                || entrada.getUbicacion() == null || entrada.getUbicacion().isBlank()) {
            throw new BusinessException("Completa código, nombre y ubicación de la sala");
        }
        if (entrada.getCapacidad() == null || entrada.getCapacidad() < 1) {
            throw new BusinessException("La capacidad debe ser superior a cero");
        }
        if (entrada.getTarifaHora() == null || entrada.getTarifaHora().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("La tarifa no puede ser negativa");
        }
        if (entrada.getActiva() == null) {
            throw new BusinessException("Selecciona el estado de la sala");
        }
        entrada.setCodigo(entrada.getCodigo().trim().toUpperCase(Locale.ROOT));
        entrada.setNombre(entrada.getNombre().trim());
        entrada.setUbicacion(entrada.getUbicacion().trim());

        boolean duplicada = entrada.getId() == null
                ? salas.existsByCodigoIgnoreCase(entrada.getCodigo())
                : salas.existsByCodigoIgnoreCaseAndIdNot(entrada.getCodigo(), entrada.getId());
        if (duplicada) {
            throw new BusinessException("Ya existe una sala con ese código");
        }
        if (entrada.getId() != null) {
            Sala actual = obtener(entrada.getId());
            actual.setCodigo(entrada.getCodigo());
            actual.setNombre(entrada.getNombre());
            actual.setUbicacion(entrada.getUbicacion());
            actual.setCapacidad(entrada.getCapacidad());
            actual.setTarifaHora(entrada.getTarifaHora());
            actual.setActiva(entrada.getActiva());
            return salas.saveAndFlush(actual);
        }
        return salas.saveAndFlush(entrada);
    }

    @Transactional
    public void eliminar(Long id) {
        // TODO bloque 1: obtener, comprobar reglas y eliminar con el repositorio.
        // En bloque 2, impedir el borrado si hay reservas asociadas.
        Sala sala = obtener(id);
        if (reservas.existsBySalaId(id)) {
            throw new BusinessException(
                    "No puedes eliminar esta sala porque tiene reservas asociadas"
            );
        }
        salas.delete(sala);
        throw new BusinessException("Eliminar sala: operación pendiente de la práctica");
    }
}
