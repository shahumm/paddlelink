package com.example.pingpong;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// LOGIC
public class Game {
    private DataOutputStream out;
    private DataInputStream in;

    public Game(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
    }

    public void start() {
        new Thread(() -> {
            try {
                int ballX = 300, ballY = 200;
                int ballVelX = 2, ballVelY = 2;
                int opponentPaddleY = 150;

                while (true) {
                    // BALL MOVEMENT
                    ballX += ballVelX;
                    ballY += ballVelY;

                    // BALL COLLIDING WITH WALLS
                    if (ballY <= 0 || ballY >= 400) {
                        ballVelY = -ballVelY;
                    }

                    // OPPONENT PADDLE POSITION
                    int paddleY = in.readInt();

                    // BALL COLLIDING WITH PADDLES
                    if (ballX <= 20 && ballY >= paddleY && ballY <= paddleY + 60) {
                        ballVelX = -ballVelX;
                    }
                    if (ballX >= 580 && ballY >= opponentPaddleY && ballY <= opponentPaddleY + 60) {
                        ballVelX = -ballVelX;
                    }

                    // BALL OUT OF BOUNDS
                    if (ballX <= 0 || ballX >= 600) {
                        ballX = 300;
                        ballY = 200;
                        ballVelX = -ballVelX;
                    }

                    out.writeInt(ballX);
                    out.writeInt(ballY);
                    out.writeInt(opponentPaddleY);

                    opponentPaddleY = Math.min(Math.max(ballY - 30, 0), 340);

                    // 60 FPS MOTION
                    Thread.sleep(16);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
