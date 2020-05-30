package com.fabric.sim.send;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class S3MessageUploader {
    private final AmazonS3 s3Client;
    private final String bucket;

    public S3MessageUploader(String accessId, String secretKey, String bucket) {
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(createCredentialsProvider(accessId, secretKey))
                .build();
        this.bucket = bucket;
    }

    private AWSStaticCredentialsProvider createCredentialsProvider(String accessId, String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessId, secretKey));
    }

    public void upload(String path) {
        File file = new File(path);
        PutObjectRequest request = new PutObjectRequest(bucket, file.getName(), file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/xml");
        request.setMetadata(metadata);
        s3Client.putObject(request);
    }
}
