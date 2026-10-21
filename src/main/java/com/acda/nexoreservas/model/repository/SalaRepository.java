package com.acda.nexoreservas.model.repository;

import com.acda.nexoreservas.model.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    // Ejemplos de consultas derivadas para el CRUD y el dashboard.
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
    long countByActivaTrue();

    // TODO bloque 3: incorporar la búsqueda con texto, ubicación, estado y capacidad.

}
