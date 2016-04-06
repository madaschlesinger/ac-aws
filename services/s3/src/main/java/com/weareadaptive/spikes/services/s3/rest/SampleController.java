package com.weareadaptive.spikes.services.s3.rest;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String BUCKET_NAME_ENV = "S3_ADDRESS";

    private S3Facade s3Facade;

    public SampleController() throws Exception {
        String bucketName = System.getenv(BUCKET_NAME_ENV);
        s3Facade = new S3Facade(bucketName);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        s3Facade.setData();
        return s3Facade.getData();
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
