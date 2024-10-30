package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;
import dev.khaliuk.cchttpserver.dto.StatusLine;
import jakarta.inject.Singleton;

@Singleton
public class NotFoundHandler implements TargetHandler {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.setStatusLine(StatusLine.notFound());
    }
}
