package org.cch.napa.exceptions;

public class SQLException extends PersistenceException {
    public SQLException() {
    }

    public SQLException(String message) {
        super(message);
    }

    public SQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLException(Throwable cause) {
        super(cause);
    }
}