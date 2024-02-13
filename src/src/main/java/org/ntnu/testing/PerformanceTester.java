package org.ntnu.testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ntnu.client.ClientSimulator;
import org.ntnu.server.MultiThreadedServer;
import org.ntnu.server.SingleThreadedServer;

public class PerformanceTester {
    private static List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

    public static synchronized void recordResponseTime(long responseTime) {
        responseTimes.add(responseTime);
    }

    public static void main(String[] args) {
        // Default values
        int numberOfClients = 10;
        boolean isMultiThreaded = true;

        if (args.length > 0) {
            try {
                numberOfClients = Integer.parseInt(args[0]);
                if (args.length > 1) {
                    isMultiThreaded = Boolean.parseBoolean(args[1]);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid argument format. Using default values.");
            }
        }

        // Start the testing process
        testServer(numberOfClients, isMultiThreaded);
    }

    public static void testServer(int numberOfClients, boolean isMultiThreaded) {
        startServer(isMultiThreaded);

        waitForServerStartup();

        System.out.println("Simulating " + numberOfClients + " clients...");
        ClientSimulator.simulateClients(numberOfClients);

        waitForClientCompletion();

        stopServer(isMultiThreaded);

        displayResults();
    }

    private static void startServer(boolean isMultiThreaded) {
        System.out.println(isMultiThreaded ? "Starting MultiThreadedServer" : "Starting SingleThreadedServer");
        if (isMultiThreaded) {
            MultiThreadedServer.startServer();
        } else {
            SingleThreadedServer.startServer();
        }
    }

    private static void waitForServerStartup() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Wait interrupted: " + e.getMessage());
        }
    }

    private static void waitForClientCompletion() {
    }

    private static void stopServer(boolean isMultiThreaded) {
        if (isMultiThreaded) {
            MultiThreadedServer.stopServer();
        } else {
            SingleThreadedServer.stopServer();
        }
        System.out.println("Server stopped.");
    }

    private static void displayResults() {
        if (responseTimes.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }
    
        long totalResponseTime = responseTimes.stream().mapToLong(Long::longValue).sum();
        double averageResponseTime = totalResponseTime / (double) responseTimes.size();
    
        System.out.println("Total clients: " + responseTimes.size());
        System.out.println("Total response time: " + totalResponseTime + " nanoseconds");
        System.out.println("Average response time: " + averageResponseTime + " nanoseconds");
    }
}
