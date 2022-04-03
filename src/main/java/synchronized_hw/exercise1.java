package synchronized_hw;
import java.lang.Thread;
public class exercise1 {
    public static void main(String[] args) {
        final long PROGRAM_START_TIME = System.nanoTime();
        Thread th0 = new Thread(() -> {
            Thread th1 = new Thread(()->{
                for(int i = 0;;){
                   synchronized (Thread.currentThread()) {
                        try {
                            Thread.currentThread().wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getStackTrace());
                        }
                    }
                    i++;
                    if(i % 5 == 0 && i != 0)
                        System.out.println("Прошло 5 секунд");
                }
            });
            th1.start();
            while(true == true){
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e.getStackTrace());
                    }
                }
                System.out.println("Time from program start: " + (System.nanoTime() - PROGRAM_START_TIME) / 1e9f + "\tseconds");
                synchronized (th1) {
                    th1.notify();
                }
            }
        });
     th0.start();
    }
}
