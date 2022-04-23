package Game;

import Game.Objects.Point;
import Game.Objects.Snake;
import Game.Objects.SnakeDirection;
import Graphics.GameGraphicsDrawer;
import Graphics.GraphicsDrawer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.List;
import java.util.Random;


public class Game implements Runnable {

    private static final int ENEMY_SNAKE_POSITION_OFFSET = 2;

    private final GameGraphicsDrawer gameDrawer;
    private final GameState state;
    private final int frameRate;
    private final int x;
    private final int y;
    private boolean exit = false;
    final Random random;

    public Game(int gridSize, int frameRate, GraphicsDrawer graphicsDrawer) {

        x = y = gridSize;
        this.frameRate = frameRate;

        state = new GameState();
        int snakeHeadX = x / 2;
        int snakeHeadY = y / 2;
        if (x % 2 == 0) snakeHeadX--;
        if (y % 2 == 0) snakeHeadY--;
        state.snake.getHead().setLocation(snakeHeadX, snakeHeadY);

        int enemySnakeHeadX = snakeHeadX + ENEMY_SNAKE_POSITION_OFFSET;
        int enemySnakeHeadY = snakeHeadY + ENEMY_SNAKE_POSITION_OFFSET;
        state.enemySnake.getHead().setLocation(enemySnakeHeadX, enemySnakeHeadY);

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

            if (state.isStarted) {
                calculateAndUpdateNextEnemyMove(state.snake.getBody(), state.enemySnake.getBody(), state.obstacle, state.fruit);
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

    public void calculateAndUpdateNextEnemyMove(List<Point> snakeBody, List<Point> enemySnakeBody, Point obstacle, Point fruit) {
        SnakeDirection randomSnakeDirection = getRandomSnakeDirection();









        state.enemySnake.setNextMoveDirection(randomSnakeDirection);
    }

    private SnakeDirection getRandomSnakeDirection() {
        int randomNumber = getRandomNumber(1, 5);
        switch (randomNumber) {
            case 2:
                return SnakeDirection.Top;
            case 3:
                return SnakeDirection.Left;
            case 4:
                return SnakeDirection.Right;
            default:
                return SnakeDirection.Down;
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

        public void update(GameState state) { // enemy moves first, then player
        var nextEnemySnakeHeadPosition = simulateMoveForward(state.enemySnake);

        if (doesCollideWithAnything(nextEnemySnakeHeadPosition, true)) {
            state.isOver = true;
            state.isWin = true;
        }

        boolean shouldCreateNewFruitAndObstacle = false;

        if (state.fruit != null && nextEnemySnakeHeadPosition.equals(state.fruit)) {
            shouldCreateNewFruitAndObstacle = true;
            state.enemySnake.moveAndExtend();
            state.enemyScore++;
        } else {
            state.enemySnake.move();
        }

        var nextSnakeHeadPosition = simulateMoveForward(state.snake);
        if (doesCollideWithAnything(nextSnakeHeadPosition, false)) {
            state.isOver = true;
            state.isWin = false;
        }


        if (state.fruit != null && nextSnakeHeadPosition.equals(state.fruit)) {
            shouldCreateNewFruitAndObstacle = true;
            state.snake.moveAndExtend();
            state.score++;
        } else {
            state.snake.move();
        }

        if(shouldCreateNewFruitAndObstacle) {
            state.fruit = null;
            state.obstacle = null;

            state.fruit = getRandomPoint();
            int minimumPointsForObstacleExistence = x * y / 4 * 3;
            if ((state.score < minimumPointsForObstacleExistence) || (state.enemyScore < minimumPointsForObstacleExistence)) {
                state.obstacle = getRandomPoint();
            }
        }

        if (state.score == (x * y) - 1) { // TODO why don't we "isOver = true" here?
            state.isOver = true;
            state.isWin = true;
        }
        if (state.enemyScore == (x * y) - 1) {
            state.isOver = true;
            state.isWin = false;
        }
    }

    private boolean doesCollideWithAnything(Point nextHeadPosition, boolean calculatingForEnemy) {

        int x = nextHeadPosition.x;
        int y = nextHeadPosition.y;

        return x < 0 || y < 0 || x >= this.x || y >= this.y // out of border
                || state.obstacle != null && nextHeadPosition.equals(state.obstacle) // collision with obstacle
                || simulatedDoesCollideWithSnake(nextHeadPosition, state.snake, calculatingForEnemy)
                || simulatedDoesCollideWithSnake(nextHeadPosition, state.enemySnake, !calculatingForEnemy); // collision with enemy snake
    }

    private boolean simulatedDoesCollideWithSnake(Point nextHeadPosition, Snake snake, boolean calculatingVsEnemy) {

        if(calculatingVsEnemy) {

            for (var snakePart: snake.getBody()) {
                if (snakePart.equals(nextHeadPosition)) {
                    return true;
                }
            }
            return false;

        } else {

            var lastBodyPart = snake.getBody().getLast();

            for (var snakePart: snake.getBody()) {
                if (snakePart != lastBodyPart && snakePart.equals(nextHeadPosition)) {
                    return true;
                }
            }
            return false;

        }
    }

    private Point simulateMoveForward(Snake snake) {

        var headClone = snake.getHead().clone();
        snake.advancePoint(headClone);
        return headClone;
    }

    private Point getRandomPoint() {

        Point point = new Point();

        do {
            point.x = random.nextInt(x);
            point.y = random.nextInt(y);
        } while (doesCollideWithSnake(point, state.snake)
                || doesCollideWithSnake(point, state.enemySnake)
                || (state.obstacle != null && point.equals(state.obstacle))
                || (state.fruit != null && point.equals(state.fruit)));

        return point;
    }

    private boolean doesCollideWithSnake(Point point, Snake snake) {

        for (var snakePart: snake.getBody()) {
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
