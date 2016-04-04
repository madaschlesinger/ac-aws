package com.weareadaptive.spikes.services.dependent.rest;

import org.springframework.web.client.RestTemplate;

public class Client {
    private final String address;

    public Client(String address) {

        this.address = address;
    }

    public String request() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String output = restTemplate.getForObject(address, String.class);
            System.out.println(String.format("Success: %s", output));
            return output;
        } catch (Exception e) {
            System.out.println(String.format("Failure: %s", e.getMessage()));
            return "Failure";
        }
    }
}
