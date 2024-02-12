package org.ntnu;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedServer {

    public static void main(String[] args) {
        System.out.println("SingleThreaded Server is running.");
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when opening the socket on port " + port);
            e.printStackTrace();
        }
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
