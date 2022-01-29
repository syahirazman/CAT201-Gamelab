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

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle ball;

    @FXML
    private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    @FXML
    private Text gameOver;

    @FXML
    private Text gameName;

    private int paddleSize = 550;

    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double bX = -1;
    double bY = -3;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        paddle.setWidth(paddleSize);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

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


    public void startGameButton()
    {
        createBricks();
        timeline.play();
    }

    @FXML
    void startGameButtonAction()
    {
        startButton.setVisible(false);
        gameOver.setVisible(false);
        gameName.setVisible(false);
        startGameButton();
    }

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
}
