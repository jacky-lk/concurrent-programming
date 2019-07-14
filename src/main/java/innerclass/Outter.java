package innerclass;

/**
 * 成员内部类
 *
 * 编译器会默认为成员内部类添加了
 * 一个指向外部类对象的引用
 *
 * 成员内部类是依赖于外部类的，
 * 如果没有创建外部类的对象，
 * 则无法对 Outter this&0 引用进行初始化赋值，
 * 也就无法创建成员内部类的对象了。
 *
 * @author 陆昆
 **/
public class Outter {
    private int status = 0;

    private int count = 0;

    private Inner getInnerInstance() {
        return new Inner();
    }

    class Inner {
        private int count = 1;

        public Inner() {
            System.out.println("Inner init");
            // 内部类可以直接访问外部类成员
            System.out.println(status);
            // 相同属性名称的默认访问内部类
            System.out.println(count);
            // 外部类.this.属性名
            System.out.println(Outter.this.count);
        }
    }

    public static void main(String[] args) {
        // 访问内部类第一种方式必须生成外部类
        Outter outter = new Outter();
        Inner inner = outter.new Inner();
        // 第二种方式通过提供方法
        Inner inner2 = outter.getInnerInstance();
    }
}
