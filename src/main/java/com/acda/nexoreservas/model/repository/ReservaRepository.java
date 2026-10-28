package com.acda.nexoreservas.model.repository;

import com.acda.nexoreservas.model.entity.EstadoReserva;
import com.acda.nexoreservas.model.entity.Reserva;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // TODO bloque 2: añadir las consultas por sala/cliente cuando existan las relaciones.
    boolean existsBySalaId(Long salaId);
    boolean existsByClienteId(Long clienteId);

    // TODO bloque 3: añadir estado, solapamiento, exclusión del propio ID y búsquedas.
    // Las consultas JPQL de la práctica se facilitarán en el enunciado.
    @Query("""
    SELECT COUNT(r) > 0
    FROM Reserva r
    WHERE r.sala.id = :salaId
      AND r.estado = com.acda.nexoreservas.model.entity.EstadoReserva.CONFIRMADA
      AND r.inicio < :fin
      AND r.fin > :inicio
    """)
    boolean hayConflicto(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
    SELECT COUNT(r) > 0
    FROM Reserva r
    WHERE r.sala.id = :salaId
      AND r.id <> :excluirId
      AND r.estado = com.acda.nexoreservas.model.entity.EstadoReserva.CONFIRMADA
      AND r.inicio < :fin
      AND r.fin > :inicio
    """)
    boolean hayConflictoExcluyendo(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("excluirId") Long excluirId
    );

    @Query("""
    SELECT r
    FROM Reserva r
    JOIN r.cliente c
    JOIN r.sala s
    WHERE (
        :q IS NULL
        OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(s.codigo) LIKE LOWER(CONCAT('%', :q, '%'))
    )
      AND (:estado IS NULL OR r.estado = :estado)
      AND (:desde IS NULL OR r.inicio >= :desde)
      AND (:hasta IS NULL OR r.inicio <= :hasta)
      AND (:salaId IS NULL OR s.id = :salaId)
      AND (:clienteId IS NULL OR c.id = :clienteId)
    """)
    List<Reserva> buscar(
            @Param("q") String q,
            @Param("estado") EstadoReserva estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            @Param("salaId") Long salaId,
            @Param("clienteId") Long clienteId,
            Sort sort
    );
}
