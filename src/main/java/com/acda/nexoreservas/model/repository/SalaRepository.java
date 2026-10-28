package com.acda.nexoreservas.model.repository;

import com.acda.nexoreservas.model.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    // Ejemplos de consultas derivadas para el CRUD y el dashboard.
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
    long countByActivaTrue();

    // TODO bloque 3: incorporar la búsqueda con texto, ubicación, estado y capacidad.
    @Query("""
    SELECT s
    FROM Sala s
    WHERE (
        :q IS NULL
        OR LOWER(s.codigo) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
    )
      AND (
        :ubicacion IS NULL
        OR LOWER(s.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%'))
    )
      AND (:activa IS NULL OR s.activa = :activa)
      AND (:capacidad IS NULL OR s.capacidad >= :capacidad)
    """)
    List<Sala> buscar(
            @Param("q") String q,
            @Param("ubicacion") String ubicacion,
            @Param("activa") Boolean activa,
            @Param("capacidad") Integer capacidad
    );
}
