package com.weareadaptive.spikes.services.jgroups.rest;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class Replicator implements AutoCloseable {
    private final JChannel channel;

    public Replicator(String clusterName) throws Exception {
        channel = new JChannel("jgroups.xml");
        channel.setReceiver(new ReceiverAdapter() {
            @Override
            public void receive(Message message) {
                System.out.println(String.format("Replicated: %s", message.getObject()));
            }
        });
        channel.connect(clusterName);
    }

    @Override
    public void close() throws Exception {
        channel.close();
    }

    public void replicate() throws Exception {
        String body = "body";
        Message message = new Message(null, null, body);
        channel.send(message);
    }
}
