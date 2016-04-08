package com.weareadaptive.spikes.services.sqs.server;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import java.util.List;

public class SqsServer {
    private final AmazonSQSClient syncClient;
    private final String queueUrl;

    public SqsServer(String queueName) {
        syncClient = new AmazonSQSClient(new InstanceProfileCredentialsProvider());
        Region region = Region.getRegion(Regions.EU_WEST_1);
        syncClient.setRegion(region);
        queueUrl = syncClient.getQueueUrl(queueName).getQueueUrl();
    }

    public void receive() throws Exception {
        while (true) {
            try {
                ReceiveMessageResult result = syncClient.receiveMessage(queueUrl);
                List<Message> messages = result.getMessages();
                for (Message message : messages) {
                    System.out.println(String.format("Message: %s", message.getBody()));
                    syncClient.deleteMessage(queueUrl, message.getReceiptHandle());
                }
            } catch (Exception e) {
                System.out.println(String.format("Failed: %s", e.getMessage()));
            }
        }
    }
}
