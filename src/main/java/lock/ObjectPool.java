package lock;

import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 对象池
 *
 * @author 陆昆
 **/
public class ObjectPool<T, R> {
    private Vector<T> pool;
    // 信号量限流
    private Semaphore semaphore;

    public ObjectPool(int size, T t) {
        pool = new Vector<T>();
        for (int i = 0;i < size;i++) {
            pool.add(t);
        }
        semaphore = new Semaphore(size);
    }

    R exec(Function<T, R> function) throws InterruptedException{
        semaphore.acquire();
        T t = null;
        try {
            t = pool.remove(0);
            return function.apply(t);
        } catch (Exception e) {

        } finally {
            semaphore.release();
            if (t != null) {
                pool.add(t);
            }
        }
        return null;
    }
}
