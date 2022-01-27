package game.brick;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

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

    private int paddleStartSize = 550;

    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double bX = -1;
    double bY = -3;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent actionEvent)
        {
            movePaddle();

            checkCollisionPaddle(paddle);
            circle.setLayoutX(circle.getLayoutX() + bX);
            circle.setLayoutY(circle.getLayoutY() + bY);

            if(!bricks.isEmpty())
            {
                bricks.removeIf(brick -> checkCollisionBrick(brick));
            } else
            {
                timeline.stop();
                gameOver.setVisible(true);
                gameOver.setText("YOU WIN!!");
                startButton.setVisible(true);
                startButton.setText("RESTART");
            }

            checkCollisionScene(scene);
            checkCollisionBottomZone();
        }
    }));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        paddle.setWidth(paddleStartSize);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    void startGameButtonAction(ActionEvent event)
    {
        startButton.setVisible(false);
        gameOver.setVisible(false);
        gameName.setVisible(false);
        startGame();
    }

    public void startGame()
    {
        createBricks();
        timeline.play();
    }

    public void checkCollisionScene(Node node)
    {
        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
        boolean leftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
        boolean bottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
        boolean topBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());

        if (rightBorder || leftBorder)
        {
            bX *= -1;
        }
        if (bottomBorder || topBorder)
        {
            bY *= -1;
        }
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


    public boolean checkCollisionBrick(Rectangle brick)
    {

        if(circle.getBoundsInParent().intersects(brick.getBoundsInParent()))
        {
            boolean rightBorder = circle.getLayoutX() >= ((brick.getX() + brick.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (brick.getX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((brick.getY() + brick.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (brick.getY() + circle.getRadius());

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


    public void movePaddle()
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

    public void checkCollisionPaddle(Rectangle paddle)
    {

        if(circle.getBoundsInParent().intersects(paddle.getBoundsInParent()))
        {
            boolean rightBorder = circle.getLayoutX() >= ((paddle.getLayoutX() + paddle.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (paddle.getLayoutX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((paddle.getLayoutY() + paddle.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (paddle.getLayoutY() + circle.getRadius());

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

    public void checkCollisionBottomZone()
    {
        if(circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent()))
        {
            timeline.stop();
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            gameOver.setVisible(true);
            gameOver.setText("GAME OVER!!");
            startButton.setVisible(true);
            startButton.setText("RESTART");

            paddle.setWidth(paddleStartSize);

            bX = -1;
            bY = -3;

            circle.setLayoutX(300);
            circle.setLayoutY(300);

        }
    }
}