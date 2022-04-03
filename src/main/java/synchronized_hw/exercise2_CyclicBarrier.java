package synchronized_hw;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.LongPredicate;

public class exercise2_CyclicBarrier {
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    public static volatile Long value = 1L;
    public static void printAndWait(String message, LongPredicate longPredicate){
        synchronized (Thread.currentThread()) {
            if (longPredicate.test(value))
                System.out.println(message);
            try{
                cyclicBarrier.await();
            } catch (InterruptedException e){
                System.out.println("ABCD interrupted await");
            } catch (BrokenBarrierException e){
                System.out.println("ABCD barrier break exception");
            }
        }
    }
    public static void print(long maxValue){
        List<Thread> threadList = List.of(
                new Thread(()->{
                    while (value <= maxValue){
                        printAndWait("fizz",(exp)->exp % 3L == 0L && exp %5L != 0L);
                    }
                    //System.out.println(Thread.currentThread() + " is finished");
                }, "A"),//number % 3 == 0 && number %5 !=0
                new Thread(()->{
                    while (value <= maxValue){
                        printAndWait("buzz",(exp)->exp % 3L != 0L && exp %5L == 0L);
                    }
                    //System.out.println(Thread.currentThread() + " is finished");
                }, "B"),//number % 3 != 0 && number %5 ==0
                new Thread(()->{
                    while (value <= maxValue){
                        printAndWait("fizzbuzz",(exp)->exp % 3L == 0L && exp %5L == 0L);
                    }
                    //System.out.println(Thread.currentThread() + " is finished");
                }, "C"),//number % 3 == 0 && number %5 ==0
                new Thread(()->{
                    while (value <= maxValue){
                        printAndWait(value + "",(exp)->exp % 3L != 0L && exp %5L != 0L);
                    }
                    //System.out.println(Thread.currentThread() + " is finished");
                }, "D") //number % 3 != 0 && number %5 !=0
        );
        for (Thread th: threadList){
            th.start();
        }
        while (value <= maxValue){
            while(cyclicBarrier.getNumberWaiting() < 4){
                synchronized (Thread.currentThread()){
                    try {
                        Thread.currentThread().wait(0,1);
                    }
                    catch (InterruptedException e){
                        System.out.println(e.getStackTrace());
                    }
                }
            }
            synchronized (value) {
                value++;
            }
            try{
                cyclicBarrier.await();
            } catch (InterruptedException e){
                System.out.println("master interrupted await");
            } catch (BrokenBarrierException e){
                System.out.println("master barrier break exception");
            }
        }
        //System.out.println("this is the end");
        cyclicBarrier.reset();//потрібен лише вкінці, щоб дозволити потокам закінчити свою роботу
    }
    public static void main(String[] args) {
            print(60L);
    }
}
