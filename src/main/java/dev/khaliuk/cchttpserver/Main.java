package dev.khaliuk.cchttpserver;

import io.avaje.inject.BeanScope;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logs from your program will appear here!");

        try (var beanScope = BeanScope.builder().build()) {
            ServerSocket serverSocket = new ServerSocket(4221);

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            var clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");

            var requestProcessor = beanScope.get(RequestProcessor.class);
            var response = requestProcessor.process(clientSocket.getInputStream());
            System.out.println("Processed response: " + response);

            clientSocket.getOutputStream().write(response.getBytes());

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
