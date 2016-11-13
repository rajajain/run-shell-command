package com.dev.factory;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

@SpringBootApplication
public class Server {

    public static final int BACKLOG = 100;
    public static final int PORT = 9010;
    public static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Server server = new Server();
        server.serviceRequest();
    }

    private void serviceRequest() {
        System.out.println("-------- Starting the server on port----------" + PORT + "------------");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG, InetAddress.getByName(HOST));
            System.out.println("-------- Started the server on port----------" + PORT + "------------");
            while (true) {
                try {

                    new Thread(new ExecuteShellCommandWorker(serverSocket.accept())).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
