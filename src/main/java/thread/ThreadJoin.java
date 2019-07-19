package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 现在有T1、T2、T3三个线程，
 * 你怎样保证T2在T1执行完后执行，
 * T3在T2执行完后执行
 * 1. 方案1 用join
 * 2. 方案2 用线程池只能产生一个线程
 * 3. 方案3 信号量
 * 4. 方案4 生产消费者
 *
 * @author 陆昆
 **/
public class ThreadJoin {
    public static void main(String[] args) {
        // join方式
        MyThread T1 = new MyThread("T01", null);
        MyThread T2 = new MyThread("T02", T1);
        MyThread T3 = new MyThread("T03", T2);
        try {
            T3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        T1.start();
        T2.start();
        T3.start();
        // 信号量
        Semaphore semaphore = new Semaphore(1);
        for (int i = 1; i < 4; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
            }
            new RunnableThread("T1" + i, semaphore).run();
        }
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 1; i < 4; i++) {
            executorService.execute(new RunnableThread("T2" + i, null));
        }
    }
}

class RunnableThread implements Runnable {
    private String name;

    private Semaphore semaphore;

    public RunnableThread(String name, Semaphore semaphore) {
        this.name = name;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        System.out.println(name + " running");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " end");
        if (semaphore != null) {
            semaphore.release();
        }
    }
}

class MyThread extends Thread {
    private Thread toJoin;

    public MyThread(String name, Thread toJoin) {
        super(name);
        this.toJoin = toJoin;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " running");
        try {
            if (toJoin != null) {
                toJoin.join();
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end");
    }
}
