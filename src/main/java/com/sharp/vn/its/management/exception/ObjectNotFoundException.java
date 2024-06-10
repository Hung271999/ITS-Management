package com.sharp.vn.its.management.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Object not found exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Object not found exception.
     */
    public ObjectNotFoundException() {
        super();
    }

    /**
     * Instantiates a new Object not found exception.
     *
     * @param message the message
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Object not found exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Object not found exception.
     *
     * @param cause the cause
     */
    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
