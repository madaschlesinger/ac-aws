package com.weareadaptive.spikes.services.s3.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController {

    public SampleController() throws Exception {
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "S3 hello world";
    }

    @RequestMapping("/health")
    @ResponseBody
    String health() {
        return "healthy";
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleController.class, args);
    }
}
