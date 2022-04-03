package concurrent_hw;
import java.util.concurrent.*;

public class exercise1 {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2);
        final long START_TIME = System.nanoTime();
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(()->{
            int counter = 1;
            while (true) {
                synchronized (Thread.currentThread()){
                    try {
                        Thread.currentThread().wait(1000L);
                    } catch (InterruptedException e){
                        System.out.println("e.getStackTrace() = " + e.getStackTrace());
                    }
                }
                long timeFromStart = System.nanoTime() - START_TIME;
                System.out.println("Time from start = "
                        + (long) ((timeFromStart / 1E9) % 1000L) + " s\t"
                        + (long) ((timeFromStart / 1E6) % 1000L) + " ms\t"
                        + (long) ((timeFromStart / 1E3) % 1000L) + " us\t"
                        + (long) ((timeFromStart) % 1000L) + " ns");
                if(counter == 5){
                    counter = 0;
                    try {
                        barrier.await();
                    } catch (InterruptedException e){
                        System.out.println(e.getStackTrace() + " in countdown");
                    } catch (BrokenBarrierException e){
                        System.out.println(e.getStackTrace() + " in countdown");
                    }
                }
                counter ++;
            }
        });
        executorService.execute(()->{
            while (true){
                try {
                    barrier.await();
                } catch (InterruptedException e){
                    System.out.println(e.getStackTrace() + " in countdown");
                } catch (BrokenBarrierException e){
                    System.out.println(e.getStackTrace() + " in countdown");
                }
                System.out.println("Прошло 5 секунд");
            }
        });
    }
}
