package com.sharp.vn.its.management.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Data validation exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataValidationException extends RuntimeException {

    /**
     * Instantiates a new Data validation exception.
     */
    public DataValidationException() {
        super();
    }

    /**
     * Instantiates a new Data validation exception.
     *
     * @param message the message
     */
    public DataValidationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Data validation exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Data validation exception.
     *
     * @param cause the cause
     */
    public DataValidationException(Throwable cause) {
        super(cause);
    }
}
