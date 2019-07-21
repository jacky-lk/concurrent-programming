package connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池测试
 *
 * @author 陆昆
 **/
public class ConnectionPoolTest {
    static ConnectionPool connectionPool = new ConnectionPool(10);
    // 保证所有线程同时执行
    static CountDownLatch start = new CountDownLatch(1);
    // 保证所有线程在主线程之前结束
    static CountDownLatch end;
    static final AtomicInteger got = new AtomicInteger(0);
    static final AtomicInteger notGot = new AtomicInteger(0);
    static final int THREAD_COUNT = 300;

    public static void main(String[] args) throws InterruptedException {
        end = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(new RunnableThread(), "RunnableThread-" + i).start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke " + (THREAD_COUNT * 20));
        System.out.println("fetch connection success " + got.get());
        System.out.println("fetch connection fail " + notGot.get());
    }

    static class RunnableThread implements Runnable {
        // 线程尝试获取connect次数
        private int count = 20;

        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {

            }
            while (count > 0) {
                try {
                    Connection connection = connectionPool.fetchConnection(500);
                    if (connection != null) {
                        got.incrementAndGet();
                        try {
                            connection.createStatement();
                            connection.commit();
                        } catch (SQLException e) {

                        } finally {
                            connectionPool.releaseConnection(connection);
                        }
                    } else {
                        notGot.incrementAndGet();
                    }
                } catch (InterruptedException e) {

                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
