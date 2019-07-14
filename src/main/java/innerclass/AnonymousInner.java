package innerclass;

/**
 * 匿名内部类
 * 唯一一种没有构造器的类
 * 匿名内部类用于继承其他类或是实现接口
 * 结构形式
 * new interface/abstractclls() {
 *      method(){
 *      }
 * }.method();
 * @author 陆昆
 **/
public class AnonymousInner {
    private String name = "AnonymousInner";

    private void showName() {
        new Inner(){
            public void print() {
                System.out.println("interface inner");
                System.out.println(name);
            }
        }.print();

        new AbstractInner() {
            public void print() {
                System.out.println("abstract inner");
                System.out.println(name);
            }
        }.print();
    }

    public static void main(String[] args) {
        new AnonymousInner().showName();
    }
}

interface Inner {
    void print();
}

abstract class AbstractInner{
    public abstract void print();
}
