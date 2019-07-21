package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 陆昆
 **/
public class DeadLock {

    public static void main(String[] args) {
        Lock lockA = new ReentrantLock();
        Lock lockB = new ReentrantLock();
        new Thread(new DeadLockThread(lockA, lockB), "Thread-Test-1").start();
        new Thread(new DeadLockThread(lockB, lockA ), "Thead-Test-2").start();
    }

    static class DeadLockThread implements Runnable{
        private Lock lockA;
        private Lock lockB;

        public DeadLockThread(Lock lockA, Lock lockB) {
            this.lockA = lockA;
            this.lockB = lockB;
        }

        @Override
        public void run() {
            lockA.lock();
            try {
                Thread.sleep(100);
                lockB.lock();
            } catch (InterruptedException e){
              e.printStackTrace();
            } finally {
                lockB.unlock();
            }
            lockA.unlock();
        }
    }
}
