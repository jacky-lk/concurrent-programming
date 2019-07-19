package thread;

import java.util.concurrent.TimeUnit;

/**
 * https://www.jianshu.com/p/4598a4c0fea3
 *
 * 线程状态展示
 * New: 线程刚被创建，还没有被执行
 * Runnable: 线程正在执行
 * Blocked: 等待其他线程释放锁
 * Waiting: 调用了wait或join方法，无限等待
 * Timed_Waiting: 调用了sleep、wait(interval)、join(interval)方法，有限等待
 *
 * @author 陆昆
 **/
public class ThreadStatte {
    // 第一个线程进入持有锁后不释放，后续线程block
    static class BlockedThread implements Runnable {
        public void run() {
            synchronized (BlockedThread.class) {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 线程处于Waiting状态
    static class WaitingThread implements Runnable {
        public void run() {
            synchronized (WaitingThread.class) {
                try {
                    WaitingThread.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 线程处于Timed_Waiting状态
    static class TimeWaitingThread implements Runnable {
        public void run() {
            while (true) {
                synchronized (TimeWaitingThread.class) {
                    try {
                        // 10s后释放
                        TimeWaitingThread.class.wait(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // 线程处于RUNNABLE状态
    static class RunnableThread implements Runnable {
        public void run() {
            while (true){

            }
        }
    }

    public static void main(String[] args) {
        new Thread(new BlockedThread(), "Blocked-Thread1").start();
        new Thread(new BlockedThread(), "Blocked-Thread2").start();
        new Thread(new WaitingThread(), "Wait-Thread").start();
        new Thread(new TimeWaitingThread(), "Time-Waiting-Thread").start();
        new Thread(new RunnableThread(), "Runnable-Thread").start();
    }
}
