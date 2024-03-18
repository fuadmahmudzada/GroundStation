module org.example.groundstationdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires commons.csv;
    requires jdk.jsobject;
    requires java.scripting;
    requires com.fazecast.jSerialComm;

    opens org.example.groundstationdemo to javafx.fxml;
    exports org.example.groundstationdemo;
}