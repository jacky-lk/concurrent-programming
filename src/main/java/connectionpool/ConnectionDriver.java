package connectionpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * @author 陆昆
 **/
public class ConnectionDriver {
    static class ConnectionHandler implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("commit")) {
                TimeUnit.MICROSECONDS.sleep(100);
            }
            return null;
        }
    }
    // Connection代理，commit时休眠100ms
    public static final Connection creteConnection() {
        return (Connection)Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(),
            new Class<?>[] {Connection.class}, new ConnectionHandler());
    }
}
