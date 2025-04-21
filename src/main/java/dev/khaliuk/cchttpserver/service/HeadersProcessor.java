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
    public boolean postProcessAndReturnConnectionCloseStatus(List<String> requestHeaders, HttpResponse httpResponse) {
        String acceptEncodingHeader = null;
        String connectionHeader = null;

        for (var header : requestHeaders) {
            if (header.toLowerCase().startsWith("accept-encoding")) {
                acceptEncodingHeader = header;
            } else if (header.toLowerCase().startsWith("connection")) {
                connectionHeader = header;
            }
        }

        if (acceptEncodingHeader != null) {
            processAcceptEncoding(acceptEncodingHeader, httpResponse);
        } else {
            populateContentLength(httpResponse);
        }

        if (connectionHeader != null) {
            return processConnectionHeader(connectionHeader, httpResponse);
        }

        return false;
    }

    private static void processAcceptEncoding(String header, HttpResponse httpResponse) {
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

    private static boolean processConnectionHeader(String header, HttpResponse httpResponse) {
        var value = header.substring(header.indexOf(':') + 1).trim();
        httpResponse.addHeader("Connection: " + value);
        return value.equalsIgnoreCase("close");
    }
}
