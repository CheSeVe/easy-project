package ru.cheseve.easyproject.exception;

public class NameAlreadyExistsException extends AlreadyExistsException {
    public NameAlreadyExistsException(String message) {
        super(message);
    }

    public NameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
