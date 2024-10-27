package dev.khaliuk.cchttpserver.service;

import dev.khaliuk.cchttpserver.dto.HttpResponse;
import jakarta.inject.Singleton;

import static dev.khaliuk.cchttpserver.Constants.CRLF;

@Singleton
public class ResponseSerializer {
    public String serialize(HttpResponse httpResponse) {
        var sb = new StringBuilder();
        sb.append(httpResponse.statusLine().toString()).append(CRLF);
        httpResponse.headers().forEach(header -> sb.append(header).append(CRLF));
        sb.append(CRLF);
        sb.append(httpResponse.responseBody());
        return sb.toString();
    }
}
