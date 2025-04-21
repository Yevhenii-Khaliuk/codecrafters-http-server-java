package dev.khaliuk.cchttpserver.dto;

public record ResponseWrapper(byte[] data, boolean isConnectionClose) {
}
