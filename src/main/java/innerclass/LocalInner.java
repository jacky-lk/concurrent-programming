package innerclass;

/**
 * 局部内部类
 * 定义在一个方法或者一个作用域里面的类
 * 局部内部类只能访问局部final变量
 * 未来解决访问变量和局部内部类可能是不同生命周期
 * 所以通过复制的方式传递，如果不是final则该值可以修改导致数据不一致
 *
 * @author 陆昆
 **/
public class LocalInner {
    public Thread getThread(final String a, String b) {
        class NewThread extends Thread {
            @Override
            public void run() {
                // 只能访问局部final变量
                System.out.println(a);
                System.out.println("run");
            }
        }
        return new NewThread();
    }

    public static void main(String[] args) {
        new LocalInner().getThread("test","test2").start();
    }
}
