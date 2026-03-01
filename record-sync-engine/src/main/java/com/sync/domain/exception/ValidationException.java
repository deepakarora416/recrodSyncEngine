package com.sync.domain.exception;

/**
 * Thrown when record validation fails.
 */
public class ValidationException extends SyncException {

    public ValidationException(String message) {
        super(message);
    }
}
