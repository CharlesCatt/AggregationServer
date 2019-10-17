import java.io.IOError;
import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.*;

/*
 *
 * adapted code from https://www.baeldung.com/java-priority-job-schedule
 * 
 */

public class PriorityThreadPool {
    private ExecutorService deQueueAndExecute;
    private ThreadPoolExecutor requestScheduler;
    private PriorityBlockingQueue<Runnable> jobQueue;
    public PriorityThreadPool() {
        requestScheduler = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
        jobQueue = new PriorityBlockingQueue<Runnable>(1, Comparator.comparing(FileRequest::getEventNo));
        deQueueAndExecute.execute(() -> {
            while (true) {
                try {
                    requestScheduler.execute(jobQueue.take());

                } catch (InterruptedException e) {
                    System.out.println(e);
                    break;
                }
            }
        });
    }

    public Future<String> submit(Callable fr) {
        jobQueue.add(fr);
        return new FutureTask<String>(fr);
    }
}