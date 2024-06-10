package com.sharp.vn.its.management.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Authentication exception.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    /**
     * Instantiates a new Authentication exception.
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Instantiates a new Authentication exception.
     *
     * @param message the message
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Authentication exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Authentication exception.
     *
     * @param cause the cause
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
