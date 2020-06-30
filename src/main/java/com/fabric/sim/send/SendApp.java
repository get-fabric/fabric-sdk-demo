package com.fabric.sim.send;

public class SendApp {

    public static void main(String[] args) {
        S3MessageUploader messageUploader = new S3MessageUploader(
                System.getenv("ACCESS_ID"), System.getenv("SECRET_KEY"), System.getenv("BUCKET"));
        messageUploader.upload(args[0]);
    }
}
