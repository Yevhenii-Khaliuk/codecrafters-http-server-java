package dev.khaliuk.cchttpserver.service;

import dev.khaliuk.cchttpserver.dto.HttpResponse;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class HeadersProcessor {
    public void process(List<String> requestHeaders, HttpResponse httpResponse) {
        requestHeaders.stream()
            .filter(header -> header.toLowerCase().startsWith("accept-encoding"))
            .findFirst()
            .ifPresent(header -> processAcceptEncoding(header, httpResponse));
    }

    private void processAcceptEncoding(String header, HttpResponse httpResponse) {
        var value = header.substring(header.indexOf(':') + 1).trim();
        if (value.toLowerCase().contains("gzip")) {
            httpResponse.addHeader("Content-Encoding: gzip");
        }
    }
}
