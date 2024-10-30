package dev.khaliuk.cchttpserver.handler;

import dev.khaliuk.cchttpserver.annotation.Controller;
import dev.khaliuk.cchttpserver.exception.DuplicateControllerException;
import io.avaje.inject.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class TargetHandlerFactory {
    private final List<TargetHandler> handlers;
    private Map<Pattern, TargetHandler> handlerMap;
    private final NotFoundHandler defaultHandler;

    @Inject
    public TargetHandlerFactory(List<TargetHandler> handlers, NotFoundHandler defaultHandler) {
        this.handlers = handlers;
        this.defaultHandler = defaultHandler;
    }

    @PostConstruct
    public void initHandlers() {
        handlerMap = handlers.stream()
            .filter(handler -> handler.getClass().isAnnotationPresent(Controller.class))
            .collect(Collectors.toMap(
                handler -> toPattern(handler.getClass().getAnnotation(Controller.class).value()),
                Function.identity(),
                (h1, h2) -> {
                    var errorMessage = "Duplicate controllers: %s and %s".formatted(h1.getClass(), h2.getClass());
                    throw new DuplicateControllerException(errorMessage);
                }));
    }

    public TargetHandler getHandler(String requestTarget) {
        return handlerMap.entrySet()
            .stream()
            .filter(entry -> entry.getKey().matcher(requestTarget).matches())
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(defaultHandler);
    }

    private Pattern toPattern(String endpoint) {
        return Pattern.compile(endpoint.replaceAll("\\{[a-zA-Z-_]*}", "[a-zA-Z-_]*"));
    }
}
