package Game;

import Game.Objects.Point;
import Game.Objects.SnakeDirection;
import Graphics.GameGraphicsDrawer;
import Graphics.GraphicsDrawer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Random;


public class Game implements Runnable {

    private final GameGraphicsDrawer gameDrawer;
    private final GameState state;
    private final int frameRate;
    private final int x;
    private final int y;
    private boolean exit = false;
    final Random random;


    public Game(int gridCellsX, int gridCellsY, int frameRate) {

        x = gridCellsX;
        y = gridCellsY;
        this.frameRate = frameRate;

        state = new GameState();
        int snakeHeadX = x / 2;
        int snakeHeadY = y / 2;
        if (x % 2 == 0) snakeHeadX--;
        if (y % 2 == 0) snakeHeadY--;
        state.snake.getHead().setLocation(snakeHeadX, snakeHeadY);

        random = new Random(System.currentTimeMillis());
        state.obstacle = getRandomPoint();
        state.fruit = getRandomPoint();

        gameDrawer = new GameGraphicsDrawer(x, y);
    }

    public Game(int gridSize, int frameRate, GraphicsDrawer graphicsDrawer) {

        x = y = gridSize;
        this.frameRate = frameRate;

        state = new GameState();
        int snakeHeadX = x / 2;
        int snakeHeadY = y / 2;
        if (x % 2 == 0) snakeHeadX--;
        if (y % 2 == 0) snakeHeadY--;
        state.snake.getHead().setLocation(snakeHeadX, snakeHeadY);

        random = new Random(System.currentTimeMillis());
        state.obstacle = getRandomPoint();
        state.fruit = getRandomPoint();

        gameDrawer = new GameGraphicsDrawer(graphicsDrawer, gridSize);
    }

    public void onKeyPressed(KeyEvent e) {

        if (!state.isStarted) {
            state.isStarted = true;
        }

        var keyCode = e.getCode();

        if (state.isOver || state.isWin) {
            if (keyCode == KeyCode.ENTER) {
                exit = true;
                return;
            }
        }

        var snake = state.snake;

        switch (keyCode) {
            case UP:
                snake.setNextMoveDirection(SnakeDirection.Top);
                break;
            case DOWN:
                snake.setNextMoveDirection(SnakeDirection.Down);
                break;
            case LEFT:
                snake.setNextMoveDirection(SnakeDirection.Left);
                break;
            case RIGHT:
                snake.setNextMoveDirection(SnakeDirection.Right);
                break;
        }
    }

    @Override
    public void run() {

        start();
    }

    public void start() {

        long intervalMs = 1000 / frameRate;

        gameDrawer.drawGame(state);

        while (!state.isOver && !state.isWin) {

            try {
                Thread.sleep(intervalMs);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            update(state);
            gameDrawer.drawGame(state);
        }

        while (!exit) {

            try {
                Thread.sleep(100);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void update(GameState state) {

        var nextHeadPosition = simulateMoveForward();

        if (doesCollideWithAnything(nextHeadPosition)) {
            state.isOver = true;
        }
        else if (state.fruit != null && nextHeadPosition.equals(state.fruit)) {
            state.snake.moveAndExtend();
            state.score++;

            state.obstacle = null;
            state.fruit = null;

            state.fruit = getRandomPoint();
            if (state.score < x * y / 4 * 3) {
                state.obstacle = getRandomPoint();
            }

            if (state.score == (x * y) - 1) {

                state.isWin = true;
            }
        }
        else {
            state.snake.move();
        }
    }

    private boolean doesCollideWithAnything(Point nextHeadPosition) {

        int x = nextHeadPosition.x;
        int y = nextHeadPosition.y;

        return x < 0 || y < 0 || x >= this.x || y >= this.y // out of border
            || state.obstacle != null && nextHeadPosition.equals(state.obstacle) // collision with obstacle
            || simulatedDoesCollideWithSnake(nextHeadPosition); // collision with snake itself
    }

    private boolean simulatedDoesCollideWithSnake(Point nextHeadPosition) {

        var lastBodyPart = state.snake.getBody().getLast();

        for (var snakePart: state.snake.getBody()) {
            if (snakePart != lastBodyPart && snakePart.equals(nextHeadPosition)) {
                return true;
            }
        }

        return false;
    }

    private Point simulateMoveForward() {

        var headClone = state.snake.getHead().clone();
        state.snake.advancePoint(headClone);
        return headClone;
    }

    private Point getRandomPoint() {

        Point point = new Point();

        do {
            point.x = random.nextInt(x);
            point.y = random.nextInt(y);
        } while (doesCollideWithSnake(point)
            || (state.obstacle != null && point.equals(state.obstacle))
            || (state.fruit != null && point.equals(state.fruit)));

        return point;
    }

    private boolean doesCollideWithSnake(Point point) {

        for (var snakePart: state.snake.getBody()) {
            if (snakePart.equals(point)) {
                return true;
            }
        }

        return false;
    }

    public Scene getScene() {
        return gameDrawer.getScene();
    }
}
