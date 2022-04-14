package Game;

import Game.Objects.Point;
import Game.Objects.Snake;

import java.util.Random;


public class GameState {

    public Snake snake;
    public Point fruit;
    public Point obstacle;
    public int score;
    public boolean isWin;
    public boolean isOver;
    public boolean isStarted;


    public GameState() {
        snake = new Snake();
        fruit = new Point();
        obstacle = new Point();
        score = 0;

        isWin = false;
        isOver = false;
        isStarted = false;
    }
}
