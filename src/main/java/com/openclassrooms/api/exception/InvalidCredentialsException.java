package com.openclassrooms.api.exception;

/**
 * Error 401
 */
public class InvalidCredentialsException extends RuntimeException{

    /**
     * Constructor for InvalidCredentialsException class
     */
    public InvalidCredentialsException() {
        super();
    }

    /**
     * Constructor for InvalidCredentialsException class
     *
     * @param message String
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
