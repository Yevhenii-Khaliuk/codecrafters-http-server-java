package dev.khaliuk.cchttpserver;

import jakarta.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Singleton
public class RequestProcessor {
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
        if (requestLineTokens.length != 3 || !requestLineTokens[1].equals("/")) {
            return "HTTP/1.1 404 Not Found\r\n\r\n";
        }

        return "HTTP/1.1 200 OK\r\n\r\n";
    }

    private String readUntilDelimiter(InputStream inputStream) throws IOException {
        var buffer = new ByteArrayOutputStream();
        var b = inputStream.read();

        while (b != 13) { // '\r' character
            buffer.write(b);
            b = inputStream.read();
        }
        inputStream.read(); // skip following '\n'

        return buffer.toString();
    }
}
