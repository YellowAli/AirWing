module datastructure.airplaneapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.mkammerer.argon2.nolibs;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires javax.mail;
    requires org.jsoup;
    requires org.json;
    requires java.net.http;
    requires okhttp3;
    requires java.desktop;
    requires org.apache.sis.referencing;


    opens datastructure.airplaneapplication to javafx.fxml;
    exports datastructure.airplaneapplication;
}