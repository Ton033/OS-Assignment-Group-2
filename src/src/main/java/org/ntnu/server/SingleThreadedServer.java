package org.ntnu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedServer {

    private static volatile boolean isRunning = false;
    private static ServerSocket serverSocket;
    private static Thread serverThread;

    public static void main(String[] args) {
        startServer();
    }

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
                System.out.println("SingleThreaded Server is running on port " + port);

                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                if (!isRunning) {
                    System.out.println("Server is stopping.");
                } else {
                    System.out.println("Exception caught when trying to listen on port " + port + ": " + e.getMessage());
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
                System.out.println("Error while closing the server socket or waiting for server thread to finish: " + e.getMessage());
            }
        }
        System.out.println("Server has been stopped.");
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
    
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                long startTime = System.currentTimeMillis();
    
                System.out.printf("Sent from the client: %s\n", inputLine);
                String[] tokens = inputLine.split(" ");
    
                if (tokens.length == 3) {
                    try {
                        double num1 = Double.parseDouble(tokens[0]);
                        double num2 = Double.parseDouble(tokens[1]);
                        String operation = tokens[2];
    
                        double result = performOperation(num1, num2, operation);
                        out.println(result);
                    } catch (IllegalArgumentException e) {
                        out.println("Error: " + e.getMessage());
                    }
                } else {
                    out.println("Invalid input format.");
                }
    
                long endTime = System.currentTimeMillis();
                System.out.println("Processing time: " + (endTime - startTime) + " ms");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static double performOperation(double num1, double num2, String operator) {
        switch (operator) {
            case "A":
                return num1 + num2;
            case "S":
                return num1 - num2;
            case "M":
                return num1 * num2;
            case "D":
                if (num2 == 0) {
                    throw new IllegalArgumentException("Error: Division by zero");
                } else {
                    return num1 / num2;
                }
            case "MOD":
                if (num2 == 0) {
                    throw new IllegalArgumentException("Error: Modulus by zero");
                } else {
                    return num1 % num2;
                }
            default:
                throw new IllegalArgumentException("Error: Invalid operation code");
        }
    }
}
