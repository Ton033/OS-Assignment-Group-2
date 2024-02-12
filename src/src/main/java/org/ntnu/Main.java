package org.ntnu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select an option:");
        System.out.println("1: Start the single-threaded server");
        System.out.println("2: Start the multi-threaded server");
        System.out.println("3: Start the client simulator");
        System.out.println("4: Start manual client for testing");
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
                ClientSimulator.simulateClients();
                break;
            case 4:
                System.out.println("Starting manual client for testing.");
                System.out.println("Enter the first number, the second number, and the operator (A for addition, S for subtraction, M for multiplication, D for division) separated by spaces.");
                System.out.println("For example: 50 20 A");
                System.out.println("To exit the client, type 'exit'");
                Client.main(new String[]{});
                break;
            default:
                System.out.println("Exiting program.");
                break;
        }

        scanner.close();
    }
}
