package Game;

import Game.Objects.Point;
import Game.Objects.Snake;

import java.util.Random;


public class GameState {

    public Snake snake;
    public Snake enemySnake;
    public Point fruit;
    public Point obstacle;
    public int score;
    public int enemyScore;
    public boolean isWin;
    public boolean isOver;
    public boolean isStarted;


    public GameState() {
        snake = new Snake();
        enemySnake = new Snake();
        fruit = new Point();
        obstacle = new Point();
        score = 0;
        enemyScore = 0;

        isWin = false;
        isOver = false;
        isStarted = false;
    }
}
