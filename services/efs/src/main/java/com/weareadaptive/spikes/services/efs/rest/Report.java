package com.weareadaptive.spikes.services.efs.rest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

class Report {
    private final String path;
    private final int id;

    Report(String folder, int id) {
        this.id = id;
        this.path = String.format("%s/report-%d.txt", folder, id);
    }

    void write(Map<String, List<String>> parameters) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(String.format("*** REPORT %d ***\r\n", id));
            writer.write(String.format("* Generated %s *\r\n\r\n", new SimpleDateFormat().format(new Date())));
            for (Map.Entry<String, List<String>> entrySet : parameters.entrySet()) {
                writer.write(entrySet.getKey() + ":\t" + entrySet.getValue() + "\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return readStream();
    }

    private String readStream() {
        if (!new File(path).exists()) {
            return "No report " + id + " exists. Expected to find at " + path;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line).append("<br/>");
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
