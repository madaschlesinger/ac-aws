package com.weareadaptive.spikes.services.sqs.server;

public class Main {
    private static final String QUEUE_NAME_ENV = "QUEUE_NAME";

    public static void main(String[] args) throws Exception {
        String queueName = System.getenv(QUEUE_NAME_ENV);
        final SqsServer server = new SqsServer(queueName);
        server.receive();
    }
}
