package dev.khaliuk.cchttpserver.service;

import dev.khaliuk.cchttpserver.dto.HttpMethod;
import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.dto.RequestLine;
import dev.khaliuk.cchttpserver.handler.TargetHandlerFactory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Singleton
public class RequestProcessor {
    private final HeadersProcessor headersProcessor;
    private final TargetHandlerFactory targetHandlerFactory;
    private final ResponseSerializer responseSerializer;

    @Inject
    public RequestProcessor(HeadersProcessor headersProcessor,
                            TargetHandlerFactory targetHandlerFactory,
                            ResponseSerializer responseSerializer) {
        this.headersProcessor = headersProcessor;
        this.targetHandlerFactory = targetHandlerFactory;
        this.responseSerializer = responseSerializer;
    }

    public byte[] process(InputStream inputStream) throws IOException {
        // 1. Request line
        var requestLine = readUntilDelimiter(inputStream);
        System.out.println("Got request line: " + requestLine);

        // 2. Headers
        var headers = new ArrayList<String>();
        var header = readUntilDelimiter(inputStream);
        while (!header.isEmpty()) {
            System.out.println("Got header: " + header);
            headers.add(header);
            header = readUntilDelimiter(inputStream);
        }

        var requestLineTokens = requestLine.split(" ");
        if (requestLineTokens.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        var requestTarget = requestLineTokens[1].trim();
        var httpRequest = HttpRequest.builder()
            .requestLine(RequestLine.builder()
                .httpMethod(HttpMethod.valueOf(requestLineTokens[0].trim()))
                .target(requestLineTokens[1].trim())
                .protocolVersion(requestLineTokens[2].trim())
                .build())
            .headers(headers)
            .inputStream(inputStream)
            .build();
        var httpResponse = HttpResponse.builder()
            .headers(new ArrayList<>())
            .build();

        targetHandlerFactory.getHandler(requestTarget).process(httpRequest, httpResponse);
        headersProcessor.postProcess(headers, httpResponse);
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
