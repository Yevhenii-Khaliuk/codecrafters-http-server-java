package dev.khaliuk.cchttpserver.dto;

import java.util.List;

public final class HttpResponse {
    private StatusLine statusLine;
    private final List<String> headers;
    private byte[] responseBody;

    public HttpResponse(StatusLine statusLine, List<String> headers, byte[] responseBody) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    public StatusLine statusLine() {
        return statusLine;
    }

    public List<String> headers() {
        return headers;
    }

    public byte[] responseBody() {
        return responseBody;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public void addHeader(String header) {
        headers.add(header);
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public static class HttpResponseBuilder {
        private StatusLine statusLine;
        private List<String> headers;
        private byte[] responseBody;

        public HttpResponseBuilder statusLine(StatusLine statusLine) {
            this.statusLine = statusLine;
            return this;
        }

        public HttpResponseBuilder headers(List<String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpResponseBuilder responseBody(byte[] responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusLine, headers, responseBody);
        }
    }
}
