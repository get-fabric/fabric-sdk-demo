package com.fabric.sim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorIncomingMessages {

    private static final String QUEUE_NAME = "fd-to-fabric-sqs";

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final SQSMessagesFetcher messageFetcher =
            new SQSMessagesFetcher(System.getenv("ACCESS_ID"), System.getenv("SECRET_KEY"), QUEUE_NAME);

    public static void main(String[] args) throws InterruptedException {
        scheduler.scheduleWithFixedDelay(messagesFetcher(), 0, 1, TimeUnit.SECONDS);
        scheduler.awaitTermination(24, TimeUnit.HOURS);
    }

    private static Runnable messagesFetcher() {
        return () -> {
            messageFetcher.processMessagesWith((String message) -> {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                System.out.println(timeStamp + " ====>");
                System.out.println(message);
            });
        };
    }
}
