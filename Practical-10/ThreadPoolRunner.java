import java.util.concurrent.*;

public class ThreadPoolRunner {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService e = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            int id = i;

            e.submit(new Runnable() {
                public void run() {
                    System.out.println("Task " + id + " running on "
                            + Thread.currentThread().getName());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
            });
        }

        e.shutdown();
        e.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("All tasks completed");
    }
}


