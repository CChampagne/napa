package org.cch.napa.exceptions;

public class RuntimePersistenceException extends RuntimeException {
    public RuntimePersistenceException() {
    }
    public RuntimePersistenceException(PersistenceException ex) {
        super(ex.getMessage(), ex.getCause());
    }

    public RuntimePersistenceException(String message) {
        super(message);
    }

    public RuntimePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimePersistenceException(Throwable cause) {
        super(cause);
    }
}