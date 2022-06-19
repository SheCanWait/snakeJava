package Game.Objects;

import Game.GameState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node {

    public List<Node> childNodes = new ArrayList<>();

    public LinkedList<Point> snakeBody;
    public LinkedList<Point> enemySnakeBody;
    public Point fruit;

    // -1 -> enemy loses
    // 0 -> game not ended
    // 1 -> enemy wins
    public int gameResult;

    @Override
    public String toString() {
        return "snake: " + snakeBody + "\n" +
                "enemy snake: " + enemySnakeBody + "\n" +
                "fruit: " + fruit + "\n" +
                "game result: " + gameResult + "\n\n";
    }

}
