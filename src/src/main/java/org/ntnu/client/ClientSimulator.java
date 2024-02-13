package org.ntnu.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSimulator {

    public static void simulateClients(int numberOfClients) {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfClients);
        CountDownLatch latch = new CountDownLatch(numberOfClients);
        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfClients; i++) {
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
        int numberOfClients = 3; // Default 
        if (args.length > 0) {
            try {
                numberOfClients = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number of clients provided, using default: " + numberOfClients);
            }
        }
        simulateClients(numberOfClients);
    }
}