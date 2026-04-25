package ru.cheseve.easyproject.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {

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
