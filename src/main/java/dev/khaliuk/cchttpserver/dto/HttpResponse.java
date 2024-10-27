package dev.khaliuk.cchttpserver.dto;

import java.util.List;

public record HttpResponse(StatusLine statusLine, List<String> headers, String responseBody) {

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    public static class HttpResponseBuilder {
        private StatusLine statusLine;
        private List<String> headers;
        private String responseBody;

        public HttpResponseBuilder statusLine(StatusLine statusLine) {
            this.statusLine = statusLine;
            return this;
        }

        public HttpResponseBuilder headers(List<String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpResponseBuilder responseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusLine, headers, responseBody);
        }
    }
}
