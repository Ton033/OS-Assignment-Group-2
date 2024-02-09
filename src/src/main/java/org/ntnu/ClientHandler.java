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
                System.out.printf(" Sent from the client: %s\n",line); out.println(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private double performOperation(double num1, double num2, String operator) {
        double sum = 0;

        switch (operator) {
            case "A":
                return num1 + num2;
            case "S":
                return num1 - num2;
            case "M":
                return num1 * num2;
            case "D":
                if (num2 <= 0 ) {
                    return 0;
                } else {
                    return num1 / num2;
                }
            default:
                throw new IllegalStateException("Invalid input");
        }

    }
}
