package dev.khaliuk.cchttpserver.dto;

public record RequestLine(HttpMethod httpMethod, String target, String protocolVersion) {
    public static RequestLineBuilder builder() {
        return new RequestLineBuilder();
    }

    public static class RequestLineBuilder {
        private HttpMethod httpMethod;
        private String target;
        private String protocolVersion;

        public RequestLineBuilder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public RequestLineBuilder target(String target) {
            this.target = target;
            return this;
        }

        public RequestLineBuilder protocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public RequestLine build() {
            return new RequestLine(httpMethod, target, protocolVersion);
        }
    }
}
