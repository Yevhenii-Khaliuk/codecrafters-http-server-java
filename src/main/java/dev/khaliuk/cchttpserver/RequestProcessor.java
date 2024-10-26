package dev.khaliuk.cchttpserver;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Singleton
public class RequestProcessor {
    private final ResponseSerializer responseSerializer;

    @Inject
    public RequestProcessor(ResponseSerializer responseSerializer) {
        this.responseSerializer = responseSerializer;
    }

    public String process(InputStream inputStream) throws IOException {
        // 1. Request line
        var requestLine = readUntilDelimiter(inputStream);
        System.out.println("Got request line: " + requestLine);

        // 2. Headers
        var headers = new ArrayList<String>();
        var header = readUntilDelimiter(inputStream);
        while (!header.equals("")) {
            System.out.println("Got header: " + header);
            headers.add(header);
            header = readUntilDelimiter(inputStream);
        }

        // 3. Optional request body
//        System.out.println("Waiting for request body");
//        var requestBody = inputStream.readAllBytes();
//        System.out.println("Request body received");

        var requestLineTokens = requestLine.split(" ");
        if (requestLineTokens.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        var requestTarget = requestLineTokens[1];

        if (requestTarget.equals("/")) {
            return "HTTP/1.1 200 OK\r\n\r\n";
        }

        if (!requestTarget.startsWith("/echo")) {
            return "HTTP/1.1 404 Not Found\r\n\r\n";
        }

        var pathVariable = requestTarget.substring(6); // "/echo/{var}"

        var responseHeaders = new ArrayList<String>();
        responseHeaders.add("Content-Type: text/plain");
        responseHeaders.add("Content-Length: " + pathVariable.length());

        var httpResponse = HttpResponse.builder()
                .statusLine(StatusLine.builder().status(HttpStatus.OK).build())
                .headers(responseHeaders)
                .responseBody(pathVariable)
                .build();

        return responseSerializer.serialize(httpResponse);
    }

    private String readUntilDelimiter(InputStream inputStream) throws IOException {
        var buffer = new ByteArrayOutputStream();
        var b = inputStream.read();

        while (b != 13) { // '\r' character
            buffer.write(b);
            b = inputStream.read();
        }
        skipNewLineCharacter(inputStream);

        return buffer.toString();
    }

    private void skipNewLineCharacter(InputStream inputStream) throws IOException {
        var b = inputStream.read();
        if (b != 10) { // '\n' character
            throw new IllegalArgumentException("CRLF delimiter wrong, got: %s after '\r'".formatted(b));
        }
    }
}
