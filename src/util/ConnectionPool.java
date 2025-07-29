package util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 7;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> realPool;

    static {
        loadDriver();
        initConnectionPool();
    }

    private ConnectionPool() {
    }


    private static void initConnectionPool() {
        String poolSize = PropertiesUtil.getKey(POOL_SIZE_KEY);
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        realPool = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(),
                    new Class[]{Connection.class}, (proxy, method, args) ->
                            method.getName().equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args));
            pool.add(proxyConnection);
            realPool.add(connection);
        }
    }

    public static Connection getPool() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getKey(URL_KEY),
                    PropertiesUtil.getKey(USER_KEY),
                    PropertiesUtil.getKey(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        try {
            for (Connection realPools : realPool) {
                realPools.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
