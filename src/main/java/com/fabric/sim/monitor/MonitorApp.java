package com.fabric.sim.monitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorApp {

    private static final String QUEUE_NAME = "fd-to-fabric-sqs-demo";

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final SQSMessagesFetcher messageFetcher =
            new SQSMessagesFetcher(System.getenv("ACCESS_ID"), System.getenv("SECRET_KEY"), QUEUE_NAME);

    public static void main(String[] args) throws InterruptedException {
        fetchMessagesEveryFiveSeconds();
    }

    private static void fetchMessagesEveryFiveSeconds() throws InterruptedException {
        scheduler.scheduleWithFixedDelay(messagesFetcher(), 0, 5, TimeUnit.SECONDS);
        scheduler.awaitTermination(24, TimeUnit.HOURS);
    }

    private static Runnable messagesFetcher() {
        return () -> {
            messageFetcher.processMessagesWith(MonitorApp::printMessageLog);
        };
    }

    private static void printMessageLog(String message) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp + " ======>");
        System.out.println(message);
        System.out.println("-----");
    }
}
