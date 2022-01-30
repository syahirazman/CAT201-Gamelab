package com.example.gamelabapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GamelabApplication extends Application {

    // create scene and stage for main application
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GamelabApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Welcome to Gamelab!");
        stage.setScene(scene);
        stage.show();
    }

    // starting point to launch the application
    public static void main(String[] args) {
        launch();
    }
}