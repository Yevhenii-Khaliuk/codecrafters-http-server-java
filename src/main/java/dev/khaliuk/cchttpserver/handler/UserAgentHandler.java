package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.annotation.Controller;
import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.dto.StatusLine;
import jakarta.inject.Singleton;

@Singleton
@Controller("/user-agent")
public class UserAgentHandler implements TargetHandler {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        var userAgentHeader = httpRequest.headers().stream()
            .filter(h -> h.toLowerCase().startsWith("user-agent"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("User-Agent header is not found"));

        var headerValue = userAgentHeader.substring(11).trim();
        httpResponse.setStatusLine(StatusLine.ok());
        httpResponse.addHeader("Content-Type: text/plain");
        httpResponse.setResponseBody(headerValue.getBytes());
    }
}
