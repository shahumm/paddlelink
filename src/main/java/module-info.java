module com.example.pingpong {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.pingpong to javafx.fxml;
    exports com.example.pingpong;
}