package Game;

import Game.Objects.Point;
import Game.Objects.Snake;


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

    public boolean doesCollideWithAnything(Point nextHeadPosition, boolean calculatingForEnemy, int x, int y) {

        int x_ = nextHeadPosition.x;
        int y_ = nextHeadPosition.y;

        return x_ < 0 || y_ < 0 || x_ >= x || y_ >= y // out of border
                || simulatedDoesCollideWithSnake(nextHeadPosition, this.snake, calculatingForEnemy)
                || simulatedDoesCollideWithSnake(nextHeadPosition, this.enemySnake, !calculatingForEnemy); // collision with enemy snake
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
}
