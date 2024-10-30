package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.annotation.Controller;
import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.dto.StatusLine;
import jakarta.inject.Singleton;

@Singleton
@Controller("/echo/{param}")
public class EchoHandler implements TargetHandler {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        var pathVariable = httpRequest.requestLine().target().substring(6); // "/echo/{var}"

        httpResponse.setStatusLine(StatusLine.ok());
        httpResponse.addHeader("Content-Type: text/plain");
        httpResponse.setResponseBody(pathVariable.getBytes());
    }
}
