package ru.cheseve.easyproject.exception;

public class ItemAlreadyExistsException extends AlreadyExistsException {
    public ItemAlreadyExistsException(String message) {
        super(message);
    }

    public ItemAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
