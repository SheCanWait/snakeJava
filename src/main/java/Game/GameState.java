package Game;

import Game.Objects.Point;
import Game.Objects.Snake;

import java.util.LinkedList;


public class GameState {

    public Snake snake;
    public Snake enemySnake;
    public Point fruit;
    public int score;
    public int enemyScore;
    public boolean isWin;
    public boolean isOver;
    public boolean isStarted;


    public GameState() {
        snake = new Snake();
        enemySnake = new Snake();
        fruit = new Point();
        score = 0;
        enemyScore = 0;

        isWin = false;
        isOver = false;
        isStarted = false;
    }

    public static boolean doesCollideWithAnything(Point nextHeadPosition, LinkedList<Point> snakeBody, LinkedList<Point> enemySnakeBody, boolean calculatingForEnemy, int x, int y) {

        int x_ = nextHeadPosition.x;
        int y_ = nextHeadPosition.y;

        return x_ < 0 || y_ < 0 || x_ >= x || y_ >= y // out of border
                || simulatedDoesCollideWithSnake(nextHeadPosition, snakeBody, calculatingForEnemy)
                || simulatedDoesCollideWithSnake(nextHeadPosition, enemySnakeBody, !calculatingForEnemy); // collision with enemy snake
    }

    private static boolean simulatedDoesCollideWithSnake(Point nextHeadPosition, LinkedList<Point> snakeBody, boolean calculatingVsEnemy) {

        if(calculatingVsEnemy) {

            for (var snakePart: snakeBody) {
                if (snakePart.equals(nextHeadPosition)) {
                    return true;
                }
            }
            return false;

        } else {

            var lastBodyPart = snakeBody.getLast();

            for (var snakePart: snakeBody) {
                if (snakePart != lastBodyPart && snakePart.equals(nextHeadPosition)) {
                    return true;
                }
            }
            return false;

        }
    }
}
