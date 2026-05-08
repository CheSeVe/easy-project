package ru.cheseve.easyproject.exception;

public class ItemNotPresentException extends NotFoundException {
    public ItemNotPresentException(String message) {
        super(message);
    }

    public ItemNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemNotPresentException(Throwable cause) {
        super(cause);
    }
}
