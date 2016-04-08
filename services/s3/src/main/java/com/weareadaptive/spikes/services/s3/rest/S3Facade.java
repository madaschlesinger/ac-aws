package com.weareadaptive.spikes.services.s3.rest;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringInputStream;
import org.springframework.util.MultiValueMap;

import java.io.*;

class S3Facade {
    private final AmazonS3Client s3client;
    private final String bucketName;

    S3Facade(String bucketName) {
        this.bucketName = bucketName;
        s3client = new AmazonS3Client(new InstanceProfileCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.EU_WEST_1));
    }

    void setData(String path, MultiValueMap parameters) {
        try {
            InputStream data = new StringInputStream(parameters.toString());
            s3client.putObject(new PutObjectRequest(bucketName, path, data, new ObjectMetadata()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    String getData(String path) {
        if (!s3client.doesObjectExist(bucketName, path)) {
            return "No data here";
        }

        S3Object data = s3client.getObject(new GetObjectRequest(bucketName, path));
        return readStream(data.getObjectContent());
    }

    private String readStream(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
