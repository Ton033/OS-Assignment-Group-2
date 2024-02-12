package org.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                System.out.printf(" Sent from the client: %s\n",line);

                String[] newLine = line.split(" "); // Breaks the input string around the space into arrays
                if (newLine.length == 3) {

                    // Turns the two first string into a double variable
                    double num1 = Double.parseDouble(newLine[0]);
                    double num2 = Double.parseDouble(newLine[1]);
                    String operation = newLine[2];

                    try {
                        // Performs the operation and returns it
                        double result = performOperation(num1, num2, operation);
                        out.println(result); // Sends result to client
                    } catch (IllegalArgumentException e) {
                        out.println("Error: " + e.getMessage()); // Sends error message to client
                    }
                } else {
                    out.println("Invalid input format."); //Sends error message to client
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }        


    /**
     * Does an operation based on the given input
     *
     *
     * @param num1 The first number
     * @param num2 The second number
     * @param operator What kind of operations the method should do
     *                 (A, S, M, D corresponding to the operation of addition,
     *                 subtraction, multiplication and division)
     *
     * @return Returns the result after the operation.
     *         If the user tries to divide by zero, return zero
     */
    private double performOperation(double num1, double num2, String operator) {
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
