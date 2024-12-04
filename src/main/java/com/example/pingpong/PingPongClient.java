package com.example.pingpong;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class PingPongClient extends Application {
    private int ballX = 300, ballY = 200;
    private int ballVelX = 2, ballVelY = 2;
    private int paddleY = 150;
    private int opponentPaddleY = 150;

    private Canvas canvas;
    private GraphicsContext gc;

    // PADDLE SPEED
    private final int paddleSpeed = 5;
    private boolean moveUp = false;
    private boolean moveDown = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(600, 400);
        gc = canvas.getGraphicsContext2D();

        // SETTING UP THE SCENE
        Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas));
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(this::handleKeyRelease);
        primaryStage.setTitle("Ping Pong Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameLoop();
            }
        }.start();
    }

    private void gameLoop() {
        // GAME LOGIC
        ballX += ballVelX;
        ballY += ballVelY;

        // BALL COLLISION WITH WALLS
        if (ballY <= 0 || ballY >= 400) {
            ballVelY = -ballVelY;
        }

        // BALL COLLISION WITH PADDLES
        if (ballX <= 20 && ballY >= paddleY && ballY <= paddleY + 60) {
            ballVelX = -ballVelX;
        }
        if (ballX >= 580 && ballY >= opponentPaddleY && ballY <= opponentPaddleY + 60) {
            ballVelX = -ballVelX;
        }

        // WHEN BALL IS OUT OF BOUNDS
        if (ballX <= 0 || ballX >= 600) {
            ballX = 300;
            ballY = 200;

            // CHANGE DIRECTION
            ballVelX = -ballVelX;
        }

        // OPPONENT THAT FOLLOWS THE BALL IN SET PATTERN
        if (ballY > opponentPaddleY + 30) {
            opponentPaddleY += 2;
        } else if (ballY < opponentPaddleY + 30) {
            opponentPaddleY -= 2;
        }

        // MOVE PADDLE BASED ON KEY PRESS
        if (moveUp && paddleY > 0) {
            paddleY -= paddleSpeed;
        }
        if (moveDown && paddleY < 340) {
            paddleY += paddleSpeed;
        }

        Platform.runLater(() -> {
            renderGame(gc);
        });
    }

    private void renderGame(GraphicsContext gc) {
        // CANVAS BACKGROUND
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // PADDLE AND BALL
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(10, paddleY, 10, 60);  // Player's paddle
        gc.fillRect(580, opponentPaddleY, 10, 60);  // Opponent's paddle
        gc.fillOval(ballX, ballY, 10, 10);
    }

    // MOVE THE PADDLE ON KEY PRESS
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            moveUp = true;
        }
        if (event.getCode() == KeyCode.DOWN) {
            moveDown = true;
        }
    }

    // STOP MOVING PADDLE WHEN KEY IS RELEASED
    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            moveUp = false;
        }
        if (event.getCode() == KeyCode.DOWN) {
            moveDown = false;
        }
    }
}
