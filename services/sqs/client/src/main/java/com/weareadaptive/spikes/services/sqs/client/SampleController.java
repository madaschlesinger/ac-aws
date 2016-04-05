package com.weareadaptive.spikes.services.sqs.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String QUEUE_NAME_ENV = "QUEUE_NAME";
    private final SqsClient client;

    public SampleController() {
        String queueName = System.getenv(QUEUE_NAME_ENV);
        client = new SqsClient(queueName);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        try {
            client.send("Test");
            return "Sent";
        } catch (Exception e) {
            return "Failed";
        }
    }

    @RequestMapping("/health")
    @ResponseBody
    String health() {
        return "healthy";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
