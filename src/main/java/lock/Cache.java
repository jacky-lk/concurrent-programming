package lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：
 * 允许多个线程同时读取共享变量
 * 只允许一个线程写共享变量
 * 一个线程在执行写操作，此时禁止线程读共享变量
 *
 * 关键点
 * 1. 获取写锁之前如果有读操作，必须先释放读锁，否则写锁会永久等待，没有所谓的锁升级
 * 2. 读写锁支持锁降级，在已经获取写锁的情况下，再次获取读锁，可以先释放写锁
 *
 * @author 陆昆
 **/
public class Cache<K, V> {
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rwl.readLock();
    private ReentrantReadWriteLock.WriteLock w = rwl.writeLock();
    private Map<K, V> cache = new HashMap();

    public V getKey(K key) {
        V result = null;
        r.lock();
        try {
            result = cache.get(key);
        } finally {
            r.unlock();
        }
        if (result != null) {
            return result;
        }
        w.lock();
        try {
            result = cache.get(key);
            if (result == null) {
                // db查获取value
                V v = null;
                result = v;
                putValue(key, v);
            }
        } finally {
            w.unlock();
        }
        return result;
    }

    public V putValue(K key, V value) {
        w.lock();
        try {
            return cache.put(key, value);
        }finally {
            w.unlock();
        }
    }
}
