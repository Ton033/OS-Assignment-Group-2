package org.ntnu;

import java.util.Scanner;

import org.ntnu.client.Client;
import org.ntnu.client.ClientSimulator;
import org.ntnu.server.MultiThreadedServer;
import org.ntnu.server.SingleThreadedServer;
import org.ntnu.testing.PerformanceTester;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select an option:");
        System.out.println("1: Start the single-threaded server");
        System.out.println("2: Start the multi-threaded server");
        System.out.println("3: Start the client simulator");
        System.out.println("4: Start manual client for testing");
        System.out.println("5: Run performance tests");
        System.out.println("Type any other number to exit");

        int input = scanner.nextInt();

        switch (input) {
            case 1:
                System.out.println("Starting the single-threaded server.");
                SingleThreadedServer.main(new String[]{});
                break;
            case 2:
                System.out.println("Starting the multi-threaded server.");
                MultiThreadedServer.main(new String[]{});
                break;
            case 3:
                System.out.println("Starting the client simulator.");
                System.out.println("Enter the number of clients to simulate:");
                int numberOfClients = scanner.nextInt();
                ClientSimulator.simulateClients(numberOfClients);
                break;
            case 4:
                System.out.println("Starting manual client for testing.");
                Client.main(new String[]{});
                break;
            case 5:
                System.out.println("Running performance tests.");
                System.out.println("Enter the number of clients for performance testing:");
                int clientsForTesting = scanner.nextInt();
                System.out.println("Use multi-threaded server? (true/false):");
                boolean isMultiThreaded = scanner.nextBoolean();
                PerformanceTester.testServer(clientsForTesting, isMultiThreaded);
                break;
            default:
                System.out.println("Exiting program.");
                break;
        }

        scanner.close();
    }
}
