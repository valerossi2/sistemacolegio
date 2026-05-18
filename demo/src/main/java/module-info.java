module com.example.demo {
    requires javafx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires mysql.connector.j;

    exports com.example.demo;
    exports model;
    exports config;
}
