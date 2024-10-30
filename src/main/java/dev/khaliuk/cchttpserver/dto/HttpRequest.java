package dev.khaliuk.cchttpserver.dto;

import java.io.InputStream;
import java.util.List;

public record HttpRequest(RequestLine requestLine, List<String> headers, InputStream inputStream) {

    public static HttpRequestBuilder builder() {
        return new HttpRequestBuilder();
    }

    public static class HttpRequestBuilder {
        private RequestLine requestLine;
        private List<String> headers;
        private InputStream inputStream;

        public HttpRequestBuilder requestLine(RequestLine requestLine) {
            this.requestLine = requestLine;
            return this;
        }

        public HttpRequestBuilder headers(List<String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequestBuilder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(requestLine, headers, inputStream);
        }
    }
}
