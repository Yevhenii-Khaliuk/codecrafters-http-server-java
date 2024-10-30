package dev.khaliuk.cchttpserver.service;

import dev.khaliuk.cchttpserver.dto.HttpResponse;
import jakarta.inject.Singleton;

import static dev.khaliuk.cchttpserver.Constants.CRLF;

@Singleton
public class ResponseSerializer {
    public byte[] serialize(HttpResponse httpResponse) {
        var sb = new StringBuilder();
        sb.append(httpResponse.statusLine().toString()).append(CRLF);
        httpResponse.headers().forEach(header -> sb.append(header).append(CRLF));
        sb.append(CRLF);

        if (httpResponse.responseBody() == null) {
            return sb.toString().getBytes();
        }

        return appendResponseBody(sb.toString().getBytes(), httpResponse.responseBody());
    }

    private byte[] appendResponseBody(byte[] responseAsBytes, byte[] responseBody) {
        var resultArray = new byte[responseAsBytes.length + responseBody.length];
        System.arraycopy(responseAsBytes, 0, resultArray, 0, responseAsBytes.length);
        System.arraycopy(responseBody, 0, resultArray, responseAsBytes.length, responseBody.length);
        return resultArray;
    }
}
