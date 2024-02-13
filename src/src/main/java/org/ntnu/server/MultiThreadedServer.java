package org.ntnu.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {

    private static volatile boolean isRunning = false;
    private static ServerSocket serverSocket = null;
    private static Thread serverThread = null;

    public static void startServer() {
        if (isRunning) {
            System.out.println("Server is already running.");
            return;
        }
        isRunning = true;
        int port = 1234;

        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("MultiThreaded Server is running on port " + port);

                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                if (!isRunning) {
                    System.out.println("Server is stopping.");
                } else {
                    System.out.println("Could not listen on port: " + port + ", " + e.getMessage());
                }
            }
        });
        serverThread.start();
    }

    public static void stopServer() {
        isRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                serverThread.join();
            } catch (IOException | InterruptedException e) {
                System.out.println("Error while closing the server socket: " + e.getMessage());
            }
        }
        System.out.println("Server has been stopped.");
    }
    public static void main(String[] args) {
        startServer();
    }
}
