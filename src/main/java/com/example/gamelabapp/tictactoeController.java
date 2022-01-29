package com.example.gamelabapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;

public class tictactoeController implements Initializable {

    //id of player 1
    @FXML
    private TextField playerName1;

    //id of player 2
    @FXML
    private TextField playerName2;

    //id of the boxes
    @FXML
    private Button box1;

    @FXML
    private Button box2;

    @FXML
    private Button box3;

    @FXML
    private Button box4;

    @FXML
    private Button box5;

    @FXML
    private Button box6;

    @FXML
    private Button box7;

    @FXML
    private Button box8;

    @FXML
    private Button box9;

    //id text displayed when finished
    @FXML
    private Text Finished;

    //id rules and player's symbol
    @FXML
    private Text Rules;

    //define player 1
    private String player1 = "Player 1";

    //define player 2
    private String player2 = "Player 2";

    //initiate the turn
    private int Turn = 0;

    //initiate condition of game over
    private int pwins = 0;

    //array list of the boxes
    ArrayList<Button> boxes;

    //define the boxes
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxes = new ArrayList<>(Arrays.asList(box1, box2, box3, box4, box5, box6, box7, box8, box9));

        boxes.forEach(box ->{
            changeButton(box);
            box.setFocusTraversable(false);
        });
    }

    //function to fetch player1's name
    @FXML
    void getName1() {
        player1 = playerName1.getText();
        setDefault();
    }

    //function to fetch player2's name
    @FXML
    void getName2() {
        player2 = playerName2.getText();
        setDefault();
    }

    //function set name as default
    void setDefault() {

        if(playerName1.getText().isEmpty()){
            player1 = "Player 1";
        }

        if(playerName2.getText().isEmpty()){
            player2 = "Player 2";
        }
    }

    //function to restart the game
    @FXML
    void restartButton() {
        boxes.forEach(this::resetBox);
        playerName1.clear();
        playerName2.clear();
        setDefault();
        Finished.setText("Let's Play!");
        Rules.setText("Rules: Make a row of XXX or OOO to win the game!");
        Turn = 0;
        pwins = 0;
    }

    @FXML
    void backToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Stage stage;
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    //function disable the box clicked by the player
    private void changeButton(Button box) {
        box.setOnMouseClicked(mouseEvent -> {
            Rules.setText(player1 + ": X,   " + player2 + ": O");
            playerSymbol(box);
            box.setDisable(true);
            checkEndGame();
        });
    }

    //function to disable all the buttons after the game finished
    void stopGame() {
        box1.setDisable(true);
        box2.setDisable(true);
        box3.setDisable(true);
        box4.setDisable(true);
        box5.setDisable(true);
        box6.setDisable(true);
        box7.setDisable(true);
        box8.setDisable(true);
        box9.setDisable(true);
    }

    //function to clear all the boxes
    void resetBox(Button box){
        box.setDisable(false);
        box.setText("");
    }

    //function to switch the player's turn and symbol
    void playerSymbol(Button box){
        if(Turn == 0){
            box.setText("X");
            Turn = 1;
        } else{
            box.setText("O");
            Turn = 0;
        }
    }

    //function to check condition if the game is over
    void checkEndGame(){
        //loop to check for every boxes
        for (int i = 0; i < 8; i++) {
            //switch condition to form i line of XXX or OOO
            String strike = switch (i) {
                case 0 -> box1.getText() + box2.getText() + box3.getText();
                case 1 -> box1.getText() + box4.getText() + box7.getText();
                case 2 -> box1.getText() + box5.getText() + box9.getText();
                case 3 -> box3.getText() + box5.getText() + box7.getText();
                case 4 -> box3.getText() + box6.getText() + box9.getText();
                case 5 -> box2.getText() + box5.getText() + box8.getText();
                case 6 -> box4.getText() + box5.getText() + box6.getText();
                case 7 -> box7.getText() + box8.getText() + box9.getText();
                default -> null;
            };

            //statement displayed if one of the player wins
            if (strike.equals("XXX")) {
                Finished.setText("Congrats! " + player1 + " wins the game!");
                stopGame();
                pwins = 1;
            }
            else if (strike.equals("OOO")) {
                Finished.setText("Congrats! " + player2 + " wins the game!");
                stopGame();
            }
        }

        //loop to check for every box is filled or not
        for (int a = 0; a < 9; a++){
            //statement displayed if the game tied
            if (box1.getText().isEmpty() || box2.getText().isEmpty() || box3.getText().isEmpty() ||
                    box4.getText().isEmpty() || box5.getText().isEmpty() || box6.getText().isEmpty() ||
                    box7.getText().isEmpty() || box8.getText().isEmpty() || box9.getText().isEmpty()){
                break;
            }
            else if (a == 8 && pwins == 1) {
                break;
            }
            else if (a == 8 && pwins == 0){
                Finished.setText("Game over! It's a tie!");
                stopGame();
            }
        }
    }
}


