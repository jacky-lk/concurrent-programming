package pattern.producer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author 陆昆
 **/
public class ProducerCakeTest {
    // 桌子上只能放3个蛋糕
    public static void main(String[] args) {
        ArrayBlockingQueue<String> table = new ArrayBlockingQueue<>(3);

        for (int i = 0; i < 3; i++) {
            new Thread(new MakerThread(table), "Maker-Thread-" + i).start();
            new Thread(new EaterThread(table), "Eater-Thread-"+i).start();
        }
    }
}

/**
 * 制作蛋糕线程
 */
class MakerThread implements Runnable {
    private static int id = 0;
    private ArrayBlockingQueue<String> table;

    public MakerThread(ArrayBlockingQueue<String> table) {
        this.table = table;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(100));
                String cake = "cake :" + getId() + " by " + Thread.currentThread().getName();
                System.out.println("make "+ cake);
                table.put(cake);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getId() {
        return id++;
    }
}

class EaterThread implements Runnable {
    private ArrayBlockingQueue<String> table;

    public EaterThread(ArrayBlockingQueue<String> table) {
        this.table = table;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String cake = table.take();
                System.out.println(Thread.currentThread().getName() + "eat " + cake);
                Thread.sleep(new Random().nextInt(100));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
