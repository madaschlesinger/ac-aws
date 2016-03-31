package com.weareadaptive.spikes.services.zeromq.server;

import org.zeromq.ZContext;

import java.io.IOException;

public class Main {
    private static final String ZMQ_PORT_ENV = "ZMQ_PORT";
    private static final String PIPE_NAME = "backend";

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv(ZMQ_PORT_ENV));

        ZContext context = new ZContext();
        Server server = new Server(port, PIPE_NAME, 3);
        Thread serverThread = new Thread(server);
        serverThread.start();

        System.out.println("Enter to exit");
        System.in.read();

        context.destroy();

    }
}
