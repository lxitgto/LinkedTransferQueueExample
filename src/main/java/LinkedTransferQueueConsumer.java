import java.util.concurrent.TransferQueue;

/**
 * Created by ethan-liu on 15/6/21.
 */
public class LinkedTransferQueueConsumer implements Runnable {

    protected TransferQueue<String> transferQueue;

    public LinkedTransferQueueConsumer(TransferQueue<String> queue) {
        this.transferQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = transferQueue.take();
                System.out.println(Thread.currentThread().getName()
                        + " take(): " + data);
                //Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}