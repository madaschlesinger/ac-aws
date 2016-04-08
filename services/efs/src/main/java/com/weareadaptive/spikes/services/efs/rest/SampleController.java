package com.weareadaptive.spikes.services.efs.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private static final String REPORT_FOLDER_ENV = "REPORT_FOLDER";
    private final String reportFolder;

    public SampleController() throws Exception {
        reportFolder = System.getenv(REPORT_FOLDER_ENV);
        if (reportFolder == null || reportFolder.isEmpty()) {
            throw new RuntimeException("REPORT_FOLDER environment variable not set");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleController.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "<h1>EFS Test</h1>Example usages<ul><li>To write report - report/[id]?field1=data1&field2=data2</li><li>To read report - report/[id]</li></ul>'";
    }

    @RequestMapping("/report/{id}")
    @ResponseBody
    String report(@RequestParam MultiValueMap<String, String> parameters, @PathVariable("id") int id) {
        Report report = new Report(reportFolder, id);
        if (!parameters.isEmpty()) {
            report.write(parameters);
        }
        return report.toString();
    }

    @RequestMapping("/health")
    @ResponseBody
    String health() {
        return "healthy";
    }
}
