module com.example.demofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.demofx to javafx.fxml;
    exports com.example.demofx;
}