package ru.cheseve.easyproject.crm.exception;

import ru.cheseve.easyproject.exception.AlreadyExistsException;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
