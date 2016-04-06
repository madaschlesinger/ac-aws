package com.weareadaptive.spikes.services.s3.rest;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringInputStream;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

    public String getData() {
        ObjectListing objects = s3client.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            return objectSummary.getKey();
        }
        return "no objects found";
    }

    public void setData() {
        try {
            InputStream data = new StringInputStream("Hello");
            ObjectMetadata metaData = new ObjectMetadata();
            s3client.putObject(new PutObjectRequest(bucketName, "key", data, metaData));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
