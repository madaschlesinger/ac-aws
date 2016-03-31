package com.weareadaptive.spikes.services.jgroups.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String JGROUPS_CLUSTER_NAME_ENV = "JGROUPS_CLUSTER_NAME";
    private final Replicator replicator;

    public SampleController() throws Exception {
        String clusterName = System.getenv(JGROUPS_CLUSTER_NAME_ENV);
        replicator = new Replicator(clusterName);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        try {
            replicator.replicate();
            return "replicated";
        } catch (Exception e) {
            return "failed";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleController.class, args);
    }
}
