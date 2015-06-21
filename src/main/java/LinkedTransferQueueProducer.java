import com.codahale.metrics.Counter;

import java.util.UUID;
import java.util.concurrent.TransferQueue;

/**
 * Created by ethan-liu on 15/6/21.
 */
public class LinkedTransferQueueProducer implements Runnable {

    protected TransferQueue<String> transferQueue;
    //final Random random = new Random();
    private int timer;
    Counter failedCounter;
    Counter counter;

    public LinkedTransferQueueProducer(TransferQueue<String> queue, int timer, Counter counter, Counter failedCounter) {
        this.transferQueue = queue;
        this.timer = timer;
        this.counter = counter;
        this.failedCounter = failedCounter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = UUID.randomUUID().toString();
//                System.out.println("Put: " + data);
//                transferQueue.transfer(data);
                boolean result = transferQueue.tryTransfer(data);
                if(!result){
                    this.failedCounter.inc();
                    System.out.println(Thread.currentThread().getName() + " Put data failed");
                }else{
                    this.counter.inc();
                    System.out.println(Thread.currentThread().getName() + " Put: " + data);
                }
                Thread.sleep(this.timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
