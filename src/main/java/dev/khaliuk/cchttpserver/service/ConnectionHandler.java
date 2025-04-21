package dev.khaliuk.cchttpserver.service;

import io.avaje.inject.Prototype;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.Socket;

@Prototype
public class ConnectionHandler {
    private final RequestProcessor requestProcessor;

    @Inject
    public ConnectionHandler(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    public void handle(Socket clientSocket) {
        try {
            while (true) {
                var response = requestProcessor.process(clientSocket.getInputStream());
                clientSocket.getOutputStream().write(response.data());
                if (response.isConnectionClose()) {
                    System.out.println("Closing connection");
                    clientSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error during request processing: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
