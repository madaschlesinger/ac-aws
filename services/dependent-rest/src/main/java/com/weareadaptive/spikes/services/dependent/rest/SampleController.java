package com.weareadaptive.spikes.services.dependent.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String ADDRESS_ENV = "ADDRESS";
    private final Client client;

    public SampleController() {
        String address = System.getenv(ADDRESS_ENV);
        client = new Client(address);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        String output = client.request();
        return String.format("Dependent: %s", output);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
