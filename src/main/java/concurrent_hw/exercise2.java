package concurrent_hw;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongPredicate;

public class exercise2 {
    private static AtomicLong counter = new AtomicLong(1);
    private static void act(String message, LongPredicate predicate, final CyclicBarrier BARRIER){
            if (predicate.test(counter.get()))
                System.out.println(message);
            try{
                BARRIER.await();
            } catch (InterruptedException e){
                System.out.println("ABCD interrupted await");
            } catch (BrokenBarrierException e){
                System.out.println("ABCD barrier break exception");
            }
    }
    public static void print(final AtomicLong maxValue){
        final ExecutorService executorService = Executors.newFixedThreadPool(4);
        final CyclicBarrier BARRIER = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                counter.addAndGet(1L);
            }
        });
        executorService.execute(()->{
            while (counter.get() <= maxValue.get()){
                act("fizz",(exp)->exp % 3L == 0L && exp %5L != 0L, BARRIER);
            }
        });
        executorService.execute(()->{
            while (counter.get() <= maxValue.get()){
                act("buzz",(exp)->exp % 3L != 0L && exp %5L == 0L, BARRIER);
            }
        });
        executorService.execute(()->{
            while (counter.get() <= maxValue.get()){
                act("fizzbuzz",(exp)->exp % 3L == 0L && exp %5L == 0L, BARRIER);
            }
        });
        executorService.execute(()->{
            while (counter.get() <= maxValue.get()){
                act(counter.get() + "",(exp)->exp % 3L != 0L && exp %5L != 0L, BARRIER);
            }
        });
        executorService.shutdown();
        if(counter.get() > maxValue.get()){
            BARRIER.reset();
        }
    }
    public static void main(String[] args) {
        print(new AtomicLong(31L));
    }
}
