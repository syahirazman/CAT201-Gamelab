package com.example.gamelabapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class brickController implements Initializable
{
    // id of Anchor Pane container
    @FXML
    private AnchorPane scene;

    // id of Ball brick breaker
    @FXML
    private Circle ball;

    // id of Ball's paddle
    @FXML
    private Rectangle paddle;

    // id of bottom zone where game is over if ball enters this zone
    @FXML
    private Rectangle bottomZone;

    // id of button to start game
    @FXML
    private Button startButton;

    // id of text Game Over
    @FXML
    private Text gameOver;

    // id of text Break It!
    @FXML
    private Text gameName;

    // paddle size at the beginning of the game
    private int paddleSize = 550;

    // define new object
    Robot robot = new Robot();

    // array list for bricks
    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double bX = -1;
    double bY = -3;

    // game flow
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent actionEvent)
        {
            controlPaddle();

            collisionPaddle(paddle);
            ball.setLayoutX(ball.getLayoutX() + bX);
            ball.setLayoutY(ball.getLayoutY() + bY);

            if(!bricks.isEmpty())
            {
                bricks.removeIf(brick -> collisionBrick(brick));
            } else
            {
                timeline.stop();
                gameOver.setVisible(true);
                gameOver.setText("YOU WIN!!");
                startButton.setVisible(true);
                startButton.setText("RESTART");
            }

            collisionBorder(scene);
            fallingBottomZone();
        }
    }));

    // initialize paddle size and game
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        paddle.setWidth(paddleSize);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    // create bricks for game
    public void createBricks()
    {
        double width = 560;
        double height = 200;

        int spaceCheck = 1;

        for (double i = height; i > 0 ; i = i - 50) {
            for (double j = width; j > 0 ; j = j - 25) {
                if(spaceCheck % 2 == 0){
                    Rectangle rectangle = new Rectangle(j,i,30,30);
                    rectangle.setFill(Color.PINK);
                    rectangle.setStroke(Color.DEEPPINK);
                    scene.getChildren().add(rectangle);
                    bricks.add(rectangle);
                }
                spaceCheck++;
            }
        }
    }

    // after Start button is clicked, bricks are created and game starts
    public void startGameButton()
    {
        createBricks();
        timeline.play();
    }

    // after Start button is clicked, the button and the texts are not visible
    @FXML
    void startGameButtonAction()
    {
        startButton.setVisible(false);
        gameOver.setVisible(false);
        gameName.setVisible(false);
        startGameButton();
    }

    // set collision border
    public void collisionBorder(Node node)
    {
        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = ball.getLayoutX() >= (bounds.getMaxX() - ball.getRadius());
        boolean leftBorder = ball.getLayoutX() <= (bounds.getMinX() + ball.getRadius());
        boolean bottomBorder = ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius());
        boolean topBorder = ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius());

        if (rightBorder || leftBorder)
        {
            bX *= -1;
        }
        if (bottomBorder || topBorder)
        {
            bY *= -1;
        }
    }

    // check collision between ball and brick, and remove the brick when collision happens
    public boolean collisionBrick(Rectangle brick)
    {

        if(ball.getBoundsInParent().intersects(brick.getBoundsInParent()))
        {
            boolean rightBorder = ball.getLayoutX() >= ((brick.getX() + brick.getWidth()) - ball.getRadius());
            boolean leftBorder = ball.getLayoutX() <= (brick.getX() + ball.getRadius());
            boolean bottomBorder = ball.getLayoutY() >= ((brick.getY() + brick.getHeight()) - ball.getRadius());
            boolean topBorder = ball.getLayoutY() <= (brick.getY() + ball.getRadius());

            if (rightBorder || leftBorder)
            {
                bX *= -1;
            }
            if (bottomBorder || topBorder)
            {
                bY *= -1;
            }

            paddle.setWidth(paddle.getWidth() - (0.07 * paddle.getWidth()));
            scene.getChildren().remove(brick);

            return true;
        }
        return false;
    }

    // control paddle movement following the mouse movement
    public void controlPaddle()
    {
        Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
        double sceneXPos = bounds.getMinX();

        double xPos = robot.getMouseX();
        double paddleWidth = paddle.getWidth();

        if(xPos >= sceneXPos + (paddleWidth/2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth/2));
        } else if (xPos < sceneXPos + (paddleWidth/2)){
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    // set paddle after collision
    public void collisionPaddle(Rectangle paddle)
    {

        if(ball.getBoundsInParent().intersects(paddle.getBoundsInParent()))
        {
            boolean rightBorder = ball.getLayoutX() >= ((paddle.getLayoutX() + paddle.getWidth()) - ball.getRadius());
            boolean leftBorder = ball.getLayoutX() <= (paddle.getLayoutX() + ball.getRadius());
            boolean bottomBorder = ball.getLayoutY() >= ((paddle.getLayoutY() + paddle.getHeight()) - ball.getRadius());
            boolean topBorder = ball.getLayoutY() <= (paddle.getLayoutY() + ball.getRadius());

            if (rightBorder || leftBorder)
            {
                bX *= -1;
            }
            if (bottomBorder || topBorder)
            {
                bY *= -1;
            }
        }
    }

    // check bottom zone
    public void fallingBottomZone()
    {
        if(ball.getBoundsInParent().intersects(bottomZone.getBoundsInParent()))
        {
            timeline.stop();
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            gameOver.setVisible(true);
            gameOver.setText("GAME OVER!!");
            startButton.setVisible(true);
            startButton.setText("RESTART");

            paddle.setWidth(paddleSize);

            bX = -1;
            bY = -3;

            ball.setLayoutX(300);
            ball.setLayoutY(300);

        }
    }

    // return to main application after clicking the Back button
    @FXML
    void backToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Stage stage;
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Welcome to Gamelab!");
        stage.setScene(scene);
        stage.show();
    }
}
