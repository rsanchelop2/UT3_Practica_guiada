package com.acda.nexoreservas.model.repository;

import com.acda.nexoreservas.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // TODO bloque 2: añadir las consultas por sala/cliente cuando existan las relaciones.
    boolean existsBySalaId(Long salaId);
    boolean existsByClienteId(Long clienteId);

    // TODO bloque 3: añadir estado, solapamiento, exclusión del propio ID y búsquedas.
    // Las consultas JPQL de la práctica se facilitarán en el enunciado.
}
