package Game.Objects;

import Game.GameState;

import java.util.LinkedList;
import java.util.List;

public class Node {

    public List<Node> childNodes;

    public LinkedList<Point> snakeBody;
    public LinkedList<Point> enemySnakeBody;
    public Point obstacle;
    public Point fruit;

    // -1 -> enemy loses
    // 0 -> game not ended
    // 1 -> enemy wins
    public int gameResult;

}
