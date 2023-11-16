package com.openclassrooms.api.exception;

/**
 * Storage error
 */
public class StorageException extends RuntimeException {

    /**
     * Constructor for StorageException class
     *
     * @param message String
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Constructor for StorageException class
     *
     * @param message String
     * @param cause Throwable
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
