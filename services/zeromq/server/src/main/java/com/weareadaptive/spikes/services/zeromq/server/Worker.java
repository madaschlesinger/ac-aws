package com.weareadaptive.spikes.services.zeromq.server;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

public class Worker implements Runnable {
    private final ZContext context;
    private final String address;
    private final int workerIndex;

    public Worker(ZContext context, String address, int workerIndex) {

        this.context = context;
        this.address = address;
        this.workerIndex = workerIndex;
    }

    @Override
    public void run() {
        Socket socket = context.createSocket(ZMQ.DEALER);
        socket.connect(address);

        while (!Thread.currentThread().isInterrupted()) {
            ZMsg message = ZMsg.recvMsg(socket);
            ZFrame reply = message.pop();
            ZFrame content = message.pop();

            System.out.println(String.format("Worker: %d, Input: %s", workerIndex, content));

            reply.send(socket, ZFrame.REUSE + ZFrame.MORE);
            content.send(socket, ZFrame.REUSE);
            reply.destroy();
            content.destroy();
        }
        context.destroy();
    }
}
