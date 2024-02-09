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
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            String line = null;


            while (!"exit".equalsIgnoreCase(line)) {
                line = sc.nextLine();
                out.println(line);
                out.flush();

                System.out.print("Server replied "+in.readLine());
            }

            sc.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not connect to server");
        } catch (IOException e) {
            throw new RuntimeException("Could not connect to server");
        }

    }

}
