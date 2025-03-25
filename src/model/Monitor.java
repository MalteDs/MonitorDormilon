import java.util.concurrent.Semaphore;

public class Monitor implements Runnable{

    public static Semaphore semaphoreMonitor = new Semaphore(0);
    public static Semaphore semaphoreOffice = new Semaphore(1);
    public static Semaphore semaphoreChair = new Semaphore(0);
    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
