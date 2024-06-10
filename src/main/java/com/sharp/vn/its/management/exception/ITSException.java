package com.sharp.vn.its.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Its exception.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ITSException extends RuntimeException {

    /**
     * Instantiates a new Its exception.
     */
    public ITSException() {
        super();
    }

    /**
     * Instantiates a new Its exception.
     *
     * @param message the message
     */
    public ITSException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Its exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ITSException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Its exception.
     *
     * @param cause the cause
     */
    public ITSException(Throwable cause) {
        super(cause);
    }
}
