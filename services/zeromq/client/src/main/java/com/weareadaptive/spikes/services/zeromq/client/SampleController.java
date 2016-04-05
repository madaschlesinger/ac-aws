package com.weareadaptive.spikes.services.zeromq.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String ZMQ_HOSTNAME_ENV = "ZMQ_HOSTNAME";
    private static final String ZMQ_PORT_ENV = "ZMQ_PORT";
    private static final int TIMEOUT = 5;
    private final Client client;

    public SampleController() throws Exception {
        String hostname = System.getenv(ZMQ_HOSTNAME_ENV);
        int port = Integer.parseInt(System.getenv(ZMQ_PORT_ENV));
        client = new Client(hostname, port);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        String input = "echo";
        try {
            String output = client.request(input, TIMEOUT);
            return String.format("Success: %s", output);
        } catch (Exception e) {
            return "Failure";
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
