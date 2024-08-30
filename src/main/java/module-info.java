module com.example.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.xml.dom;
    requires java.naming;


    opens com.example.chess to javafx.fxml;
    exports com.example.chess;
}