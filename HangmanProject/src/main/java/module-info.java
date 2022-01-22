module com.example.hangmanproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hangmanproject to javafx.fxml;
    exports com.example.hangmanproject;
}