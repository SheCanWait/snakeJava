package Game;

import Game.Objects.Node;
import Game.Objects.Point;
import Game.Objects.Snake;
import Game.Objects.SnakeDirection;
import Graphics.GameGraphicsDrawer;
import Graphics.GraphicsDrawer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.util.*;

import static Game.Algorithms.PathFinder.getPath;


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
                calculateAndUpdateNextEnemyMove(state.enemySnake);
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

    public void calculateAndUpdateNextEnemyMove(Snake enemySnake) {
        MinMax minMax = new MinMax();
        Node root = minMax.constructTree(state, x, y, 4);

        List<Point> Path = getPath(state.enemySnake.getHead(), state.fruit, Arrays.asList(state.snake, state.enemySnake), x, y);
        state.enemySnake.setNextMoveDirection(SnakeDirection.get(enemySnake.getHead(), Path.get(0)));
    }

    // Integer.MIN_VALUE -> we win
    // -1 -> we get fruit
    // 0 -> game not ended
    // 1 -> AI gets fruit
    // Integer.MAX_VALUE -> AI wins
    private int getMinMaxValueFromNode(Node node, int currentMinMaxValue) {
        if(currentMinMaxValue == 1) {
            return currentMinMaxValue;
        }
        for(Node child : node.childNodes) {
            if(child.childNodes.size() > 0) {
                getMinMaxValueFromNode(child, currentMinMaxValue);
            } else {
                if(child.gameResult > currentMinMaxValue) {
                    currentMinMaxValue = child.gameResult;
                }
            }
        }
        return currentMinMaxValue;
    }

    public void update(GameState state) { // enemy moves first, then player
        var nextEnemySnakeHeadPosition = simulateMoveForward(state.enemySnake);

        if (state.doesCollideWithAnything(nextEnemySnakeHeadPosition, state.snake.getBody(), state.enemySnake.getBody(), true, x, y)) {
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
        if (state.doesCollideWithAnything(nextSnakeHeadPosition, state.snake.getBody(), state.enemySnake.getBody(), false, x, y)) {
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

            state.fruit = getRandomPoint();
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
}
