package thread;

import java.util.concurrent.CountDownLatch;

/**
 * 龟兔赛跑线程模拟
 *
 * (1)兔子每 0.1 秒 5 米的速度，每跑20米休息1秒;
 * (2)乌龟每 0.1 秒跑 2 米，不休息；
 * (3)其中一个跑到终点后另一个不跑了！
 *
 * @author 陆昆
 **/
public class TurtleRabbit {
    // 线程运动开关
    private static volatile boolean cancel = false;
    // 保证两者同时起跑
    private static CountDownLatch start = new CountDownLatch(1);
    // 保证主线程不提前结束
    private static CountDownLatch end = new CountDownLatch(2);

    public static void main(String[] args) {
        new Thread(new TurtleRabbit.TurtleThread(100),"TurtleThread-1").start();
        new Thread(new TurtleRabbit.RabbitThread(100),"RabbitThread-1").start();
        try {
            start.countDown();
            end.await();
        } catch (InterruptedException e) {

        }
        System.out.println("competition end!!");
    }

    /**
     * 模拟乌龟线程
     */
    static class TurtleThread implements Runnable {
        private int miles;

        public TurtleThread(int miles) {
            this.miles = miles;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {

            }
            while (!TurtleRabbit.cancel) {
                try {
                    System.out.println(Thread.currentThread().getName() + " remaining miles: "+miles);
                    if (miles <= 0) {
                        System.out.println(Thread.currentThread().getName() + " win");
                        TurtleRabbit.cancel=true;
                        break;
                    }
                    Thread.sleep(100);
                    miles -= 2;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            end.countDown();
        }
    }

    /**
     * 模拟兔子线程
     */
    static class RabbitThread implements Runnable {
        private int miles;

        public RabbitThread(int miles) {
            this.miles = miles;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {

            }
            while (!TurtleRabbit.cancel) {
                try {
                    System.out.println(Thread.currentThread().getName() + " remaining miles: "+miles);
                    if (miles <= 0) {
                        System.out.println(Thread.currentThread().getName() + " win");
                        TurtleRabbit.cancel=true;
                        break;
                    }
                    Thread.sleep(100);
                    miles -= 5;
                    if (miles > 0 && miles % 20 == 0) {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            end.countDown();
        }
    }
}
