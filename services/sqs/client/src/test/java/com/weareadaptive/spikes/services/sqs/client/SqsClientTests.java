package com.weareadaptive.spikes.services.sqs.client;

import org.junit.Test;

public class SqsClientTests {
    @Test
    public void testSend() throws Exception {
        SqsClient client = new SqsClient("terraform-queue");
        client.send("Test");
    }
}
