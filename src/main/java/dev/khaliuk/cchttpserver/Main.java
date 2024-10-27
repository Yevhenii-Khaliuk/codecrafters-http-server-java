package dev.khaliuk.cchttpserver;

import dev.khaliuk.cchttpserver.service.ConnectionHandler;
import io.avaje.config.Config;
import io.avaje.inject.BeanScope;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import static dev.khaliuk.cchttpserver.Constants.CLIENT_CONNECTION_POOL_SIZE_CONFIG;

@SuppressWarnings("java:S2189")
public class Main {
    public static void main(String[] args) {
        System.out.println("Logs from your program will appear here!");

        var connectionPoolSize = Integer.parseInt(Config.get(CLIENT_CONNECTION_POOL_SIZE_CONFIG));

        try (var beanScope = BeanScope.builder().build();
             var executorService = Executors.newFixedThreadPool(connectionPoolSize);
             var serverSocket = new ServerSocket(4221)) {
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            if (args.length >= 2 && args[0].equals("--directory")) {
                beanScope.get(ApplicationArguments.class).setFileDirectoryName(args[1]);
            }

            while (true) {
                var clientSocket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");

                executorService.execute(() -> beanScope.get(ConnectionHandler.class).handle(clientSocket));
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
