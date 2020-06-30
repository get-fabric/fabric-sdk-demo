package com.fabric.sim.monitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class


MonitorApp {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Timer timer = new Timer();
    private static final SQSMessagesFetcher messageFetcher =
            new SQSMessagesFetcher(System.getenv("ACCESS_ID"), System.getenv("SECRET_KEY"), System.getenv("SQS"));

    public static void main(String[] args) throws InterruptedException {
        timer.scheduleAtFixedRate(new MessagesFetcherTask(), 0, 5000);
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

    private static class MessagesFetcherTask extends TimerTask {

        @Override
        public void run() {
            messageFetcher.processMessagesWith(MonitorApp::printMessageLog);
        }
    }
}
