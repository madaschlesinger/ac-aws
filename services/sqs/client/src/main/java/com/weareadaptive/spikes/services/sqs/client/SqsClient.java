package com.weareadaptive.spikes.services.sqs.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class SqsClient {
    private final AmazonSQSClient syncClient;
    private final String queueUrl;

    public SqsClient(String queueName) {
        AWSCredentials credentials = new EnvironmentVariableCredentialsProvider().getCredentials();
        syncClient = new AmazonSQSClient(credentials);
        Region region = Region.getRegion(Regions.EU_WEST_1);
        syncClient.setRegion(region);
        queueUrl = syncClient.getQueueUrl(queueName).getQueueUrl();
    }

    public void send(String body) {
        SendMessageResult result = syncClient.sendMessage(queueUrl, body);
        String messageId = result.getMessageId();
        System.out.println(String.format("Sent: %s", messageId));
    }
}
