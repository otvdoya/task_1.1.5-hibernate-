package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Year;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import jm.task.core.jdbc.model.User;


public class Util {
    // реализуйте настройку соеденения с БД

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/my_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Util instance;
    private static Connection connection;
    private static SessionFactory factory;
    private Util(){}

    public static Connection getConnection() {
        Util localInstance = instance;
        if (localInstance == null) {
            synchronized (Util.class) {
                localInstance = instance;
                if (localInstance == null && connection == null) {
                    try {
                        Class.forName(DB_DRIVER);
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    } catch (ClassNotFoundException | SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    public static SessionFactory getFactory(){

        Util localInstance = instance;
        if (localInstance == null) {
            synchronized (Util.class) {
                localInstance = instance;
                if (localInstance == null && factory == null) {
                    try {
                        Configuration config = new Configuration();
                        Properties settings = new Properties();
                        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                        settings.put(Environment.URL, URL);
                        settings.put(Environment.USER, USER);
                        settings.put(Environment.PASS, PASSWORD);
                        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                        settings.put(Environment.SHOW_SQL, "true");
                        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                        settings.put(Environment.HBM2DDL_AUTO, "");
                        config.setProperties(settings);
                        config.addAnnotatedClass(User.class);
                        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
                        factory = config.buildSessionFactory(serviceRegistry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return factory;
    }
}
