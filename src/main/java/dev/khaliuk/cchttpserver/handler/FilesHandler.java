package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.ApplicationArguments;
import dev.khaliuk.cchttpserver.annotation.Controller;
import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.dto.HttpStatus;
import dev.khaliuk.cchttpserver.dto.StatusLine;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
@Controller("/files/{fileName}")
public class FilesHandler implements TargetHandler {
    private final ApplicationArguments applicationArguments;

    @Inject
    public FilesHandler(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        var fileName = httpRequest.requestLine().target().substring(7);
        var filePath = Paths.get(applicationArguments.getFileDirectoryName(), fileName);

        var method = httpRequest.requestLine().httpMethod();
        switch (method) {
            case GET -> handleGetMethod(filePath, httpResponse);
            case POST -> handlePostMethod(httpRequest, filePath, httpResponse);
            default -> throw new IllegalArgumentException(
                "No handler defined for HTTP method: %s".formatted(method));
        }
    }

    private void handleGetMethod(Path filePath, HttpResponse httpResponse) throws IOException {
        if (Files.exists(filePath)) {
            var fileContent = Files.readAllBytes(filePath);

            httpResponse.setStatusLine(StatusLine.ok());
            httpResponse.addHeader("Content-Type: application/octet-stream");
            httpResponse.setResponseBody(fileContent);
        } else {
            httpResponse.setStatusLine(StatusLine.notFound());
        }
    }

    private static void handlePostMethod(HttpRequest httpRequest, Path filePath, HttpResponse httpResponse) throws IOException {
        var contentLength = Integer.parseInt(httpRequest.headers().stream()
            .filter(h -> h.toLowerCase().startsWith("content-length"))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Content-Length header should be present for HTTP method POST"))
            .substring(15)
            .trim());

        var requestBody = httpRequest.inputStream().readNBytes(contentLength);
        Files.write(filePath, requestBody);

        httpResponse.setStatusLine(StatusLine.builder().status(HttpStatus.CREATED).build());
    }
}
