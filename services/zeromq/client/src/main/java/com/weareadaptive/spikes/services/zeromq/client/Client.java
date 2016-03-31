package com.weareadaptive.spikes.services.zeromq.client;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMsg;

import java.util.concurrent.TimeoutException;

public class Client {
    private final String hostname;
    private final int port;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String request(String input, int timeoutSecond) throws TimeoutException {
        ZContext context = new ZContext();
        Socket socket = context.createSocket(ZMQ.DEALER);
        String address = String.format("tcp://%s:%d", hostname, port);
        socket.connect(address);

        socket.send(input);

        Poller poller = new ZMQ.Poller(1);
        poller.register(socket, Poller.POLLIN);
        if (poller.poll(timeoutSecond * 1000) == -1) {
            throw new TimeoutException();
        }
        ZMsg message = ZMsg.recvMsg(socket);
        String output = message.popString();
        return output;
    }
}
