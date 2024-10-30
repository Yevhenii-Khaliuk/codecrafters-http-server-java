package dev.khaliuk.cchttpserver.service;

import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.exception.IoRuntimeException;
import jakarta.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Singleton
public class HeadersProcessor {
    public void postProcess(List<String> requestHeaders, HttpResponse httpResponse) {
        requestHeaders.stream()
            .filter(header -> header.toLowerCase().startsWith("accept-encoding"))
            .findFirst()
            .ifPresentOrElse(
                header -> processAcceptEncoding(header, httpResponse),
                () -> populateContentLength(httpResponse));
    }

    private void processAcceptEncoding(String header, HttpResponse httpResponse) {
        var value = header.substring(header.indexOf(':') + 1).trim();
        if (value.toLowerCase().contains("gzip")) {
            httpResponse.addHeader("Content-Encoding: gzip");

            try {
                var byteArrayOutputStream = new ByteArrayOutputStream();
                var gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                gzipOutputStream.write(httpResponse.responseBody());
                gzipOutputStream.close();

                httpResponse.addHeader("Content-Length: " + byteArrayOutputStream.size());
                httpResponse.setResponseBody(byteArrayOutputStream.toByteArray());

                System.out.println("Compressed body size: " + byteArrayOutputStream.size());
                System.out.println("Compressed body content: " + Arrays.toString(byteArrayOutputStream.toByteArray()));
            } catch (IOException e) {
                throw new IoRuntimeException(e.getMessage());
            }
        }
    }

    private static void populateContentLength(HttpResponse httpResponse) {
        if (httpResponse.responseBody() != null) {
            httpResponse.addHeader("Content-Length: " + httpResponse.responseBody().length);
        }
    }
}
