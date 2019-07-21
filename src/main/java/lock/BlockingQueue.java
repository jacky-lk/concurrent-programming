package lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现一个阻塞队列
 *
 * @author 陆昆
 **/
public class BlockingQueue<T> {
    // 容量大小
    private int capacity;

    private List<T> queue = new ArrayList<>();

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public T take(long timeout) throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " take wait ");
                queue.wait(timeout);
            }
            T result = null;
            if (!queue.isEmpty()) {
                result = queue.remove(0);
                System.out.println(Thread.currentThread().getName() + " take " + result);
                queue.notify();
            }
            return result;
        }
    }

    public boolean put(T t, long timeout) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() >= capacity) {
                System.out.println(Thread.currentThread().getName() + " put wait ");
                queue.wait(timeout);
            }
            boolean result = false;
            if (queue.size() < capacity) {
                result = queue.add(t);
                System.out.println(Thread.currentThread().getName() + " put  " + t);
                queue.notify();
            }
            return result;
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> queue = new BlockingQueue<>(3);
        for (int i = 0; i < 5;i++) {
            new TakeThread("Take-Thread-"+i, queue).start();
            new PutThread("Put-Thread-"+i, queue).start();
        }
    }

    static class TakeThread extends Thread {
        private volatile boolean iscancel = false;

        private BlockingQueue blockingQueue;

        public TakeThread(String name, BlockingQueue blockingQueue) {
            super(name);
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while (!iscancel) {
                try {
                    blockingQueue.take(10);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setIscancel(boolean iscancel) {
            this.iscancel = iscancel;
        }
    }

    static class PutThread extends Thread {
        private volatile boolean iscancel = false;

        private static AtomicInteger count = new AtomicInteger();

        private BlockingQueue<String> blockingQueue;

        public PutThread(String name, BlockingQueue blockingQueue) {
            super(name);
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while (!iscancel) {
                try {
                    blockingQueue.put("res: " + count.incrementAndGet(), 10);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setIscancel(boolean iscancel) {
            this.iscancel = iscancel;
        }
    }
}
