package com.example.gamelabapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeController {
    @FXML
    private Button game1;

    @FXML
    private Button game2;

    @FXML
    private Button game3;

    @FXML
    void playGame1(ActionEvent event) throws IOException {
        if(event.getSource().equals(game1)){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("tictactoe.fxml"));
            Stage stage;
            stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle("XOXO - TicTacToe Game");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void playGame2(ActionEvent event) throws IOException {
        if(event.getSource().equals(game2)){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("hangman.fxml"));
            Stage stage;
            stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle("Guess4Life - Hangman Game");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void playGame3(ActionEvent event) throws IOException {
        if(event.getSource().equals(game3)){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("brick.fxml"));
            Stage stage;
            stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle("Break it! - Brick Game");
            stage.setScene(scene);
            stage.show();
        }
    }
}