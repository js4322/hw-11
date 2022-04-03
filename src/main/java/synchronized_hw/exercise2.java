package synchronized_hw;

import java.util.List;
import java.util.function.LongPredicate;

public class exercise2 {
    public static volatile long value = 1L;
    public static void printAndWait(String message, LongPredicate longPredicate){
        synchronized (Thread.currentThread()) {
            if (longPredicate.test(value))
                System.out.println(message);

            //System.out.print(".");
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
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
        synchronized (Thread.currentThread()){
            try {
                Thread.currentThread().wait(100);
            }
            catch (InterruptedException e){
                System.out.println(e.getStackTrace());
            }
        }
        while (value <= maxValue + 1){
            synchronized (Thread.currentThread()){
                value++;
            }
            for (Thread th: threadList) {
                synchronized (th) {
                    th.notify();
                }
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(0, 1);
                    } catch (InterruptedException e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
            print(60L);
    }
}
