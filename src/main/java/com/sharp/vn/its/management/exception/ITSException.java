package com.sharp.vn.its.management.exception;

public class ITSException extends RuntimeException {

    public ITSException() {
        super();
    }

    public ITSException(String message) {
        super(message);
    }

    public ITSException(String message, Throwable cause) {
        super(message, cause);
    }

    public ITSException(Throwable cause) {
        super(cause);
    }
}
