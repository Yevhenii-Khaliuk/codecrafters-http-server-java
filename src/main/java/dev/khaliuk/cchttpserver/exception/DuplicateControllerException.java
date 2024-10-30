package dev.khaliuk.cchttpserver.exception;

public class DuplicateControllerException extends RuntimeException {
    public DuplicateControllerException(String message) {
        super(message);
    }
}
