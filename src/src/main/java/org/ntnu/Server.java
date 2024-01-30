package org.ntnu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public void Server() {

        ServerSocket server = null;

        try {
            server = new ServerSocket(12345);

            while (true) {
                Socket client = server.accept();

                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                ClientHandler clientSock = new ClientHandler(client);

                new Thread(clientSock).start();


            }

        } catch (IOException e) {
            if (server != null) {
                try {
                    server.close();
                }catch (IOException e1) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        }
    }
    }

