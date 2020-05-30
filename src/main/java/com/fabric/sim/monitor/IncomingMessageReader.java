package com.fabric.sim.monitor;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IncomingMessageReader {

    private final AmazonS3 s3Client;

    public IncomingMessageReader(AWSCredentialsProvider credentialsProvider) {
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .build();
    }

    public String readFrom(String notification) {
        JsonElement parser = JsonParser.parseString(notification);
        JsonObject s3Element = parser.getAsJsonObject()
                .get("Records").getAsJsonArray().get(0).getAsJsonObject()
                .get("s3").getAsJsonObject();

        String bucket = s3Element.get("bucket").getAsJsonObject().get("name").getAsString();
        String key = s3Element.get("object").getAsJsonObject().get("key").getAsString();
        return toString(s3Client.getObject(new GetObjectRequest(bucket, key)));
    }

    private String toString(S3Object messageObject) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(messageObject.getObjectContent()));
        StringBuilder body = new StringBuilder();
        String line = "";
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                System.out.println("Failed to parse message " + messageObject.getKey());
                continue;
            }
            body.append(line).append("\n");
        }
        return body.toString();
    }
}

