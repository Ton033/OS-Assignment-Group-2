package org.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ClientSimulator {

    private static final int NUMBER_OF_CLIENTS = 10;

    public static void simulateClients() {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_CLIENTS);
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_CLIENTS);
        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            executor.submit(new ClientTask(i, latch));
        }

        try {
            latch.await();
            long endTime = System.nanoTime();
            System.out.println("All clients have finished. Total time: " + (endTime - startTime) + " nanoseconds.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        simulateClients();
    }

    private static class ClientTask implements Runnable {

        private final int clientId;
        private final CountDownLatch latch;
        private static final String[] OPERATIONS = {"A", "S", "M", "D", "MOD"};
        private static final Random RANDOM = new Random();

        public ClientTask(int clientId, CountDownLatch latch) {
            this.clientId = clientId;
            this.latch = latch;
        }

        @Override
        public void run() {
            try (Socket socket = new Socket("localhost", 1234)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
                int num1 = RANDOM.nextInt(100) + 1;
                int num2 = RANDOM.nextInt(100) + 1;
                String operation = OPERATIONS[RANDOM.nextInt(OPERATIONS.length)];
    
                String request = num1 + " " + num2 + " " + operation;
                out.println(request);
                out.flush();
                String response = in.readLine();
    
                System.out.println("Client " + clientId + " received response: " + response);
            } catch (UnknownHostException e) {
                System.err.println("Client " + clientId + ": Could not connect to server: Unknown host.");
            } catch (IOException e) {
                System.err.println("Client " + clientId + ": Could not connect to server: IO exception.");
            } finally {
                latch.countDown();
            }
        }
    }
}
