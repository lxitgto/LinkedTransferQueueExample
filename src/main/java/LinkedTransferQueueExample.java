/**
 * Created by ethan-liu on 15/6/21.
 */

import com.codahale.metrics.*;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

public class LinkedTransferQueueExample {

    static final MetricRegistry metrics = new MetricRegistry();

    static void startConsoleReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(60, TimeUnit.SECONDS);
    }
    static void startCSVReport() {
        CsvReporter reporter = CsvReporter.forRegistry(metrics)
                .formatFor(Locale.US)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(new File("/Users/ethan-liu/IdeaProjects/LinkedTransferQueueExample/"));
        reporter.start(60, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        //Meter requests = metrics.meter("requests");
        Counter counter = metrics.counter("counter");
        Counter failedCounter = metrics.counter("failedCounter");

        final TransferQueue<String> transferQueue = new LinkedTransferQueue<String>();

        LinkedTransferQueueProducer queueProducer1 = new LinkedTransferQueueProducer(transferQueue, 20, counter, failedCounter);
        new Thread(queueProducer1).start();

        LinkedTransferQueueProducer queueProducer2 = new LinkedTransferQueueProducer(transferQueue, 10, counter, failedCounter);
        new Thread(queueProducer2).start();

        startCSVReport();

        LinkedTransferQueueConsumer queueConsumer = new LinkedTransferQueueConsumer(transferQueue);
        new Thread(queueConsumer).start();
    }
}