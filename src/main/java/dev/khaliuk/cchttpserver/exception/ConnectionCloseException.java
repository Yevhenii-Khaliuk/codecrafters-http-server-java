package dev.khaliuk.cchttpserver.exception;

public class ConnectionCloseException extends RuntimeException {
    public ConnectionCloseException(String message) {
        super(message);
    }
}
