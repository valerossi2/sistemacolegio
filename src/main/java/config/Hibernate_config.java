package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class Hibernate_config {

    public static SessionFactory sessionFactory;

    private Hibernate_config (){}

    public static void init () {
        if (sessionFactory != null) return;

        Properties props= new Properties();

        props.setProperty("hibernate.connection.driver_class",
                "com.mysql.cj.jdbc.Driver");
        props.setProperty("hibernate.connection.url",
                "jdbc:mysql://localhost:3306/sis_colegio?useSSL=false&serverTimezone=UTC");
        props.setProperty("hibernate.connection.username" ,
                System.getenv("sis_colegio_user"));
        props.setProperty("hibernate.connection.password" ,
                System.getenv("DhArMa@_478"));

        props.setProperty("hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto",
                "validate");
        props.setProperty("hibernate.show_sql",
                "true");
        props.setProperty("hibernate.format_sql",
                "true");

        // Pool de conexiones c3p0
        props.setProperty("hibernate.c3p0.min_size",  "3");
        props.setProperty("hibernate.c3p0.max_size",  "10");
        props.setProperty("hibernate.c3p0.timeout",   "300");
        props.setProperty("hibernate.c3p0.max_statements", "50");

        Configuration cfg = new Configuration()
                .setProperties(props);

        // Aquí iremos agregando los modelos
        // cuando los creemos en el siguiente paso
        // .addAnnotatedClass(Rol.class)
        // .addAnnotatedClass(Usuario.class)
        // ...

        sessionFactory = cfg.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null)
            throw new IllegalStateException(
                    "Hibernate no inicializado. Llama a HibernateConfig.init() primero."
            );
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed())
            sessionFactory.close();
    }
}

