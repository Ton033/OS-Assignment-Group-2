package org.ntnu.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.ntnu.testing.PerformanceTester;

public class ClientTask implements Runnable {
    private final int clientId;
    private final CountDownLatch latch;
    private static final String[] OPERATIONS = {"A", "S", "M", "D", "MOD"};
    private static final Random RANDOM = new Random();
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public ClientTask(int clientId, CountDownLatch latch) {
        this.clientId = clientId;
        this.latch = latch;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            int num1 = RANDOM.nextInt(100) + 1;
            int num2 = RANDOM.nextInt(100) + 1;
            String operation = OPERATIONS[RANDOM.nextInt(OPERATIONS.length)];
            String request = num1 + " " + num2 + " " + operation;
            
            long startTime = System.nanoTime();
            
            out.println(request);
            String response = in.readLine();
            
            long endTime = System.nanoTime();
            PerformanceTester.recordResponseTime(endTime - startTime);
            
            System.out.println("Client " + clientId + " received response: " + response);
        } catch (Exception e) {
            System.err.println("Client " + clientId + ": Error during communication: " + e.getMessage());
        } finally {
            latch.countDown();
        }
    }
}
