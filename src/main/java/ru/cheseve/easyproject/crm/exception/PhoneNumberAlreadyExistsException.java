package ru.cheseve.easyproject.crm.exception;

import ru.cheseve.easyproject.exception.AlreadyExistsException;

public class PhoneNumberAlreadyExistsException extends AlreadyExistsException {

    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }

    public PhoneNumberAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
