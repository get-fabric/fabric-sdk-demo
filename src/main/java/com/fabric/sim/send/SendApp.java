package com.fabric.sim.send;

public class SendApp {


    private static final String BUCKET = "fabric-to-fd-bucket-demo";

    public static void main(String[] args) {
        S3MessageUploader messageUploader = new S3MessageUploader("", "", BUCKET);
        messageUploader.upload(args[1]);
    }
}
