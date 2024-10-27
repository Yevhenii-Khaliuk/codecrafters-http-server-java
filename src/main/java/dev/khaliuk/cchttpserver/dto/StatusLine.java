package dev.khaliuk.cchttpserver.dto;

import dev.khaliuk.cchttpserver.Constants;

public record StatusLine(String protocolVersion, HttpStatus status) {
    public static StatusLineBuilder builder() {
        return new StatusLineBuilder();
    }

    public String toString() {
        return "%s %s %s".formatted(protocolVersion, status.getCode(), status.getReason());
    }

    public static class StatusLineBuilder {
        private HttpStatus status;

        public StatusLineBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public StatusLine build() {
            return new StatusLine(Constants.PROTOCOL_VERSION, status);
        }
    }
}
