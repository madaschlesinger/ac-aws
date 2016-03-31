package com.weareadaptive.spikes.services.zeromq.server;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class Server implements Runnable {
    private final int port;
    private final String pipeName;
    private final int numWorkers;

    public Server(int port, String pipeName, int numWorkers) {
        this.port = port;
        this.pipeName = pipeName;
        this.numWorkers = numWorkers;
    }

    @Override
    public void run() {
        ZContext context = new ZContext();

        Socket frontend = context.createSocket(ZMQ.ROUTER);
        String frontAddr = String.format("tcp://*:%d", port);
        frontend.bind(frontAddr);

        Socket backend = context.createSocket(ZMQ.DEALER);
        String backAddr = String.format("inproc://%s", pipeName);
        backend.bind(backAddr);

        for (int i=0; i<numWorkers; ++i){
            Worker worker = new Worker(context, backAddr, i);
            Thread workerThread = new Thread(worker);
            workerThread.start();
        }

        ZMQ.proxy(frontend, backend, null);

        context.destroy();
    }
}
