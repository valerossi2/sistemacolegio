module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires mysql.connector.j;

    exports com.example.demo.application;
    exports com.example.demo.controller;
    exports com.example.demo.theme;
    exports com.example.demo.config;
    opens com.example.demo.controller to javafx.fxml;

}
