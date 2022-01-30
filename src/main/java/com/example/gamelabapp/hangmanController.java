package com.example.gamelabapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class hangmanController implements Initializable {

    @FXML
    private Text drawingOfHangman; // the state of hangman each time player guess wrongly

    @FXML
    private TextField playerInput; // to get the input from player

    @FXML
    private Text guessingSpace; // display the word length of the hidden word and the correct guessed alphabet

    @FXML
    private Text gameOverMessage; // message displayed when the game end

    // list of asian countries that will be generated randomly
    private static final String[] words = {
            "russia", "china", "india", "kazakhstan", "saudi arabia", "iran", "mongolia",
            "indonesia", "pakistan", "turkey", "myanmar", "afghanistan", "yemen", "thailand",
            "turkmenistan", "uzbekistan", "iraq", "japan", "vietnam", "malaysia", "oman",
            "philippines", "laos", "kyrgyzstan", "syria", "cambodia", "bangladesh", "nepal",
            "tajikistan", "north korea", "south korea", "jordan", "united arab emirates",
            "azerbaijan", "georgia", "sri lanka", "egypt", "bhutan", "taiwan", "armenia",
            "israel", "kuwait", "east timor", "qatar", "lebanon", "cyprus", "palestine",
            "brunei", "bahrain", "singapore", "maldives"
    };

    private String wordToBeGuessed;  //declare string of words to be guessed

    private final StringBuilder hiddenWord = new StringBuilder();  //define object

    private int usedLives = 0;  //initialize usedLive

    //array list for hangman states when player guesses the word
    ArrayList<String> hangmanState = new ArrayList<>(Arrays.asList(
            """
            +---+
            |   |
                |
                |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
                |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
            |   |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|   |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           /    |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           / \\  |
                |
          ========="""
    ));

    @Override // initialize the game
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawingOfHangman.setText(hangmanState.get(usedLives));
        gameOverMessage.setText("Press START to guess the name of asian countries!");
    }

    @FXML // generate the random word from array of strings if word to be guessed is null, else player can start guessing
    void startGame() {
        gameOverMessage.setText("");
        playerInput.clear();
        if(wordToBeGuessed == null){
            wordToBeGuessed = words[new Random().nextInt(words.length)];
            createWord();
        }
        else{
            startGuessing();
        }
    }

    // create the hidden word to be guessed from the generated random word
    public void createWord(){
        int wordLength = wordToBeGuessed.length();
        hiddenWord.append("-".repeat(wordLength));
        guessingSpace.setText(String.valueOf(hiddenWord));
    }

    // game flow
    @FXML
    public void startGuessing(){
        String playerGuess = this.playerInput.getText();
        playerInput.clear();
        ArrayList<Integer> letterPosition = new ArrayList<>();
        char[] wordChars = wordToBeGuessed.toCharArray();
        char letterGuess = playerGuess.charAt(0);

        if(wordToBeGuessed.contains(playerGuess)){
            for (int i = 0; i < wordToBeGuessed.length(); i++) {
                if(wordChars[i] == letterGuess){
                    letterPosition.add(i);
                }
            }
            letterPosition.forEach(pos -> hiddenWord.setCharAt(pos,letterGuess));
            guessingSpace.setText(String.valueOf(hiddenWord));

            if(wordToBeGuessed.contentEquals(hiddenWord)) {
                gameOverMessage.setText("Congrats! You WIN :)");
                playerInput.setDisable(true);
            }

        } else {
            drawingOfHangman.setText(hangmanState.get(++usedLives));
            if(usedLives == 6){
                gameOverMessage.setText("Game over! You LOST :(");
                playerInput.setDisable(true);
                guessingSpace.setText(wordToBeGuessed);
            }
        }
    }

    @FXML // reset the game
    void newGame() {
        wordToBeGuessed = null;
        hiddenWord.setLength(0);
        usedLives = 0;
        drawingOfHangman.setText(hangmanState.get(0));
        playerInput.clear();
        playerInput.setDisable(false); // the playerInput button is enable again
        guessingSpace.setText("");
        gameOverMessage.setText("Game has been RESET! Press START to play a new game :)");
    }

    // return to main application after Back button is clicked
    @FXML
    void backtoHome(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("home.fxml"));
        Stage stage;
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome to Gamelab!");
        stage.setScene(scene);
        stage.show();
    }
}
