module game.brick {
    requires javafx.controls;
    requires javafx.fxml;


    opens game.brick to javafx.fxml;
    exports game.brick;
}