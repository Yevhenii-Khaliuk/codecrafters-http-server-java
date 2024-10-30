package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.dto.HttpRequest;
import dev.khaliuk.cchttpserver.dto.HttpResponse;

import java.io.IOException;

public interface TargetHandler {
    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
