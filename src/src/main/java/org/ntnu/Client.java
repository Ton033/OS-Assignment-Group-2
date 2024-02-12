package org.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client {

    /**
     * Tries to connect to the server with a host and a port
     *
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            System.out.println("Connected to server. Type 'exit' to quit.");
            String line = null;

            while (!"exit".equalsIgnoreCase(line)) {
                line = sc.nextLine();

                long startTime = System.currentTimeMillis();
                out.println(line);
                out.flush();
                String response = in.readLine();
                long endTime = System.currentTimeMillis();

                System.out.println("Server response: " + response);
                System.out.println("Round trip time: " + (endTime - startTime) + " ms");
            }

            System.out.println("Disconnected from server.");
        } catch (UnknownHostException e) {
            System.err.println("Could not connect to server: Unknown host.");
        } catch (IOException e) {
            System.err.println("Could not connect to server: IO exception.");
        }
    }
}
