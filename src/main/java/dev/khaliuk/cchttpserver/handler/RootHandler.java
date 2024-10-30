package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.annotation.Controller;
import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.dto.StatusLine;
import jakarta.inject.Singleton;

@Singleton
@Controller("/")
public class RootHandler implements TargetHandler {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.setStatusLine(StatusLine.ok());
    }
}
