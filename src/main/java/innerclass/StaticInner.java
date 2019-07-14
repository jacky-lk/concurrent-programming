package innerclass;

import java.util.ArrayList;
import java.util.List;

/**
 * 静态内部类
 * 与前三种内部类不同
 * 静态内部类不会持有外部类当前对象的引用，
 * 所以在静态内部类中无法访问外部类的非static成员
 *
 * 可以在不创建外部类对象的情况下创建内部类的对象
 * @author 陆昆
 **/
public class StaticInner {
    private Integer id;
    private Integer empLevel;

    public StaticInner(Builder builder) {
        this.id = builder.id;
        this.empLevel = builder.empLevel;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "StaticInner{" +
            "id=" + id +
            ", empLevel=" + empLevel +
            '}';
    }

    public static final class Builder{
        private Integer id;
        private Integer empLevel;

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder empLevel(Integer val) {
            empLevel = val;
            return this;
        }
    }

    public static void main(String[] args) {
        List<StaticInner> staticInners = new ArrayList<StaticInner>();
        for (int i = 0; i < 10;i++) {
            staticInners.add(new StaticInner(StaticInner.newBuilder().empLevel(i).id(i)));
        }
        System.out.println(staticInners);
    }
}
