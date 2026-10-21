package com.acda.nexoreservas.model.service;

import com.acda.nexoreservas.exception.BusinessException;
import com.acda.nexoreservas.exception.ResourceNotFoundException;
import com.acda.nexoreservas.model.entity.Cliente;
import com.acda.nexoreservas.model.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ClienteService {
    // TODO bloque 4: lista blanca para Sort, con dirección ASC/DESC.
    private static final Set<String> ORDENES = Set.of("nombre", "apellidos", "email", "activo");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final ClienteRepository clientes;

    public ClienteService(ClienteRepository clientes) {
        this.clientes = clientes;
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscar(String q, Boolean activo, String orden, String direccion) {
        // TODO bloques 3 y 4: consulta por texto/estado y ordenación.
        return clientes.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente obtener(Long id) {
        return clientes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente solicitado no existe"));
    }

    @Transactional(readOnly = true)
    public List<Cliente> todos() {
        return clientes.findAll();
    }

    @Transactional(readOnly = true)
    public long total() {
        return clientes.count();
    }

    @Transactional
    public Cliente guardar(Cliente entrada) {
        // TODO bloque 1: validar, normalizar y comprobar email duplicado.
        // Si id != null, obtener el cliente existente antes de modificarlo.

        if (entrada == null
                || entrada.getNombre() == null || entrada.getNombre().isBlank()
                || entrada.getApellidos() == null || entrada.getApellidos().isBlank()) {
            throw new BusinessException("El nombre y los apellidos son obligatorios.");
        }

        String emailNormalizado = entrada.getEmail() == null
                ? ""
                : entrada.getEmail().trim().toLowerCase(Locale.ROOT);
        entrada.setEmail(emailNormalizado);

        if (emailNormalizado.isBlank() || !EMAIL_PATTERN.matcher(emailNormalizado).matches()) {
            throw new BusinessException("El formato del email no es válido.");
        }
        if (entrada.getActivo() == null) {
            throw new BusinessException("El estado del cliente (activo/inactivo) es obligatorio.");
        }

        entrada.setNombre(entrada.getNombre().trim());
        entrada.setApellidos(entrada.getApellidos().trim());
        if (entrada.getTelefono() != null) {
            entrada.setTelefono(entrada.getTelefono().trim());
        }

        boolean duplicado = entrada.getId() == null
                ? clientes.existsByEmailIgnoreCase(entrada.getEmail())
                : clientes.existsByEmailIgnoreCaseAndIdNot(entrada.getEmail(), entrada.getId());

        if (duplicado) {
            throw new BusinessException("Ya existe un cliente con ese email.");
        }

        Cliente clienteDestino;
        if (entrada.getId() == null) {
            clienteDestino = new Cliente();
        } else {
            clienteDestino = obtener(entrada.getId());
        }

        clienteDestino.setNombre(entrada.getNombre());
        clienteDestino.setApellidos(entrada.getApellidos());
        clienteDestino.setEmail(entrada.getEmail());
        clienteDestino.setTelefono(entrada.getTelefono());
        clienteDestino.setActivo(entrada.getActivo());

        return clientes.save(clienteDestino);
    }

    @Transactional
    public void eliminar(Long id) {
        // TODO bloque 1: obtener, comprobar reglas y eliminar.
        // En bloque 2, impedir el borrado si hay reservas asociadas.
        throw new BusinessException("Eliminar cliente: operación pendiente de la práctica");
    }
}
