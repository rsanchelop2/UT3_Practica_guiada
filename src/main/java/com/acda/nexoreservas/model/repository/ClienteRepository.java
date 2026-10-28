package com.acda.nexoreservas.model.repository;

import com.acda.nexoreservas.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // TODO bloque 1: añadir consultas derivadas para detectar email duplicado,
    // tanto al crear como al editar (excluyendo el ID actual).

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);




    // TODO bloque 3: incorporar la búsqueda por texto y estado.
    @Query("""
    SELECT c
    FROM Cliente c
    WHERE (
        :q IS NULL
        OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.email) LIKE LOWER(CONCAT('%', :q, '%'))
    )
      AND (:activo IS NULL OR c.activo = :activo)
    """)
    List<Cliente> buscar(
            @Param("q") String q,
            @Param("activo") Boolean activo
    );
}
