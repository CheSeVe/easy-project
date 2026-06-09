package ru.cheseve.easyproject.integration.subscriprion.exception;

public class SubsClientException extends RuntimeException {

    public SubsClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubsClientException(String message) {
        super(message);
    }
}
