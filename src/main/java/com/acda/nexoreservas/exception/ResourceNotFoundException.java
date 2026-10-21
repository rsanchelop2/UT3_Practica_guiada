package com.acda.nexoreservas.exception;

// Señala una entidad inexistente; GlobalControllerAdvice convierte esta
// excepción en la vista de error con respuesta HTTP 404.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
