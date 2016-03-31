package com.weareadaptive.spikes.services.zeromq.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTests {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 4967;
    private static final int TIMEOUT = 5;

    @Test
    public void testRequest() throws Exception {
        Client client = new Client(HOSTNAME, PORT);
        String input = "echo";
        String output = client.request(input, TIMEOUT);
        assertEquals(input, output);
    }
}
