package com.acda.nexoreservas.model.repository;

import com.acda.nexoreservas.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // TODO bloque 1: añadir consultas derivadas para detectar email duplicado,
    // tanto al crear como al editar (excluyendo el ID actual).

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);




    // TODO bloque 3: incorporar la búsqueda por texto y estado.
}
