package com.weareadaptive.spikes.services.s3.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String BUCKET_NAME_ENV = "S3_BUCKET";

    private S3Facade s3Facade;

    public SampleController() throws Exception {
        String bucketName = System.getenv(BUCKET_NAME_ENV);
        s3Facade = new S3Facade(bucketName);
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleController.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "<h1>S3 data test</h1>Example usages<ul><li>To write data - your/data/path?field1=data1&field2=data2</li><li>To read data - your/data/path</li></ul>'";
    }

    @RequestMapping("/**")
    @ResponseBody
    String data(@RequestParam MultiValueMap parameters, HttpServletRequest request) {
        String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString().substring(1);
        if (!parameters.isEmpty()) {
            s3Facade.setData(path, parameters);
        }
        return s3Facade.getData(path);
    }

    @RequestMapping("/health")
    @ResponseBody
    String health() {
        return "healthy";
    }
}
