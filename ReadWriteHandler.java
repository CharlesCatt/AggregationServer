// For checking the queue of read/write requests and starting their execution
import java.net.*;
import java.io.*;
import java.util.concurrent.PriorityBlockingQueue;

public class ReadWriteHandler implements Runnable {

    private AggregationServer parentThread;
    public ReadWriteHandler(AggregationServer parent){
        parentThread = parent;
    }
    public void run() {
        while(true) {
            Thread t = parentThread.PQ.poll();
            if (t != null) {
                System.out.println("starting service thread");
                try {
                    t.start();
                    t.join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }

        }
    }
}
