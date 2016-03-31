package com.weareadaptive.spikes.services.indexed.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController implements AutoCloseable {
    private static final String ZK_ADDRESS_VAR = "ZK_ADDR";
    private static final String POOL_PATH_VAR = "POOL_PATH";
    private final IndexPool pool;
    private final int index;

    public SampleController() throws Exception {
        String address = System.getenv(ZK_ADDRESS_VAR);
        pool = new IndexPool(address);
        pool.start();
        String poolPath = System.getenv(POOL_PATH_VAR);
        index = pool.acquireIndex(poolPath);
    }

    @Override
    public void close() throws Exception {
        pool.close();
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return String.format("Pool index: %d", index);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
