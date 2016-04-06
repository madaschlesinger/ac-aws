package com.weareadaptive.spikes.services.s3.rest;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringInputStream;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.util.List;

/**
 * Created by spencerward on 06/04/2016.
 */
public class S3Facade {
    private final AmazonS3Client s3client;
    private String bucketName;

    public S3Facade(String bucketName) {
        this.bucketName = bucketName;
        s3client = new AmazonS3Client(new EnvironmentVariableCredentialsProvider());
        Region region = Region.getRegion(Regions.EU_WEST_1);
        s3client.setRegion(region);
    }

    public String getData(String path) {
        try {
            if (!s3client.doesObjectExist(bucketName, path)) {
                return "No data here";
            }

            S3Object data = s3client.getObject(new GetObjectRequest(bucketName, path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(data.getObjectContent()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(String path, MultiValueMap parameters) {
        try {
            InputStream data = new StringInputStream(parameters.toString());
            ObjectMetadata metaData = new ObjectMetadata();
            s3client.putObject(new PutObjectRequest(bucketName, path, data, metaData));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
