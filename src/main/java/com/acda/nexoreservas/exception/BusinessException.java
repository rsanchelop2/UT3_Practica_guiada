package com.acda.nexoreservas.exception;

// Expresa una regla de negocio incumplida. Así el repositorio se ocupa de
// persistir y el controlador puede mostrar el mensaje al volver al formulario.
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
