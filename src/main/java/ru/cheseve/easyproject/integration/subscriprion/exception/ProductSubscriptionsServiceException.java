package ru.cheseve.easyproject.integration.subscriprion.exception;

public class ProductSubscriptionsServiceException extends RuntimeException{
    public ProductSubscriptionsServiceException(String message) {
        super(message);
    }

    public ProductSubscriptionsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductSubscriptionsServiceException(Throwable cause) {
        super(cause);
    }
}
