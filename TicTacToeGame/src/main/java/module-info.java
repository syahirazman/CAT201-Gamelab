module sample.tictactoegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tictactoe to javafx.fxml;
    exports com.example.tictactoe;
}