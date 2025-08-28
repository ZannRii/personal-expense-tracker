module org.example.personalexpensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.jdbc;
    requires com.zaxxer.hikari;
    requires static lombok;
    requires spring.tx;
    requires mysql.connector.j;
    requires java.desktop;
    requires java.sql;


    opens org.example.personalexpensetracker to javafx.fxml;
    exports org.example.personalexpensetracker;
    exports org.example.personalexpensetracker.controller;
    exports org.example.personalexpensetracker.entity;
    opens org.example.personalexpensetracker.controller to javafx.fxml;
}