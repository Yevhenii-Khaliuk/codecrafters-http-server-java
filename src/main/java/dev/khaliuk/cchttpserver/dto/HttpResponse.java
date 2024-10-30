package dev.khaliuk.cchttpserver.dto;

import java.util.List;
import java.util.Objects;

public final class HttpResponse {
    private StatusLine statusLine;
    private final List<String> headers;
    private String responseBody;

    public HttpResponse(StatusLine statusLine, List<String> headers, String responseBody) {
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

    public String responseBody() {
        return responseBody;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public void addHeader(String header) {
        headers.add(header);
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (HttpResponse) obj;
        return Objects.equals(this.statusLine, that.statusLine) &&
            Objects.equals(this.headers, that.headers) &&
            Objects.equals(this.responseBody, that.responseBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusLine, headers, responseBody);
    }

    @Override
    public String toString() {
        return "HttpResponse[" +
            "statusLine=" + statusLine + ", " +
            "headers=" + headers + ", " +
            "responseBody=" + responseBody + ']';
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
