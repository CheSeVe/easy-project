package ru.cheseve.easyproject.crm.exception;

import ru.cheseve.easyproject.exception.AlreadyExistsException;

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
