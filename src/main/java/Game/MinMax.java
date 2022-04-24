package Game;

import Game.Objects.Node;
import Game.Objects.Point;
import Game.Objects.SnakeDirection;
import Game.Objects.Tree;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static Game.Objects.SnakeDirection.*;

public class MinMax {
    Tree tree;
    int x;
    int y;

    static List<Node> calculateChildNodes(Node node) {
        List<Point> possibleSnakeHeadPositions = calculatePossibleSnakePositions(node.snakeBody);
        List<Point> possibleEnemySnakeHeadPositions = calculatePossibleSnakePositions(node.enemySnakeBody);

        List<Node> childNodes = new ArrayList<>();
        for(int i = 0 ; i < possibleSnakeHeadPositions.size() ; i++) {
            for(int j = 0 ; j < possibleEnemySnakeHeadPositions.size() ; j++) {
                Node childNode = new Node();
                childNode.fruit = node.fruit;
                childNode.obstacle = node.obstacle;

                LinkedList<Point> snakeBodyCopy = (LinkedList<Point>)(node.snakeBody.clone());
                snakeBodyCopy.removeLast();
                snakeBodyCopy.addFirst(possibleSnakeHeadPositions.get(i));
                childNode.snakeBody = snakeBodyCopy;

                LinkedList<Point> enemySnakeBodyCopy = (LinkedList<Point>)(node.enemySnakeBody.clone());
                enemySnakeBodyCopy.removeLast();
                enemySnakeBodyCopy.addFirst(possibleEnemySnakeHeadPositions.get(j));
                childNode.enemySnakeBody = enemySnakeBodyCopy;

                childNodes.add(childNode);
            }
        }
        return childNodes;
    }

    private static List<Point> calculatePossibleSnakePositions(LinkedList<Point> snakeBody) {
        Point snakeHead = snakeBody.getFirst();
        List<Point> snakePossibleNextHeadPositions = Arrays.asList(
                new Point(snakeHead.x + 1, snakeHead.y),
                new Point(snakeHead.x - 1, snakeHead.y),
                new Point(snakeHead.x, snakeHead.y + 1),
                new Point(snakeHead.x, snakeHead.y - 1));
        if(snakeBody.size() > 1) {
            Point secondSnakeElement = snakeBody.get(1);
            snakePossibleNextHeadPositions.remove(secondSnakeElement);
        }
        return snakePossibleNextHeadPositions;
    }

    public void constructTree(GameState state, int x, int y) {
        this.x = x;
        this.y = y;
        Node root = new Node();
        root.snakeBody = state.snake.getBody();
        root.enemySnakeBody = state.enemySnake.getBody();
        root.obstacle = state.obstacle;
        root.fruit = state.fruit;

        tree = new Tree();
        tree.root = root;
        constructTree(root);
    }

    private void constructTree(Node parentNode) {
        List<Node> childNodes = calculateChildNodes(parentNode);

        childNodes.forEach(childNode -> {
            parentNode.childNodes.add(childNode);
            int gameOver = isGameOver(childNode);
            if(gameOver == 0) {
                constructTree(childNode);
            }
        });
    }

    // -1 -> enemy loses
    // 0 -> game not ended
    // 1 -> enemy wins
    private int isGameOver(Node node) {
       int result = 0;
       GameState state = new GameState();
       if(state.doesCollideWithAnything(node.snakeBody.getFirst(), false, x, y)) { // TODO idk if true / false should be inverted here
           result = 1;
       }
       if(state.doesCollideWithAnything(node.enemySnakeBody.getFirst(), true, x, y)) {
           result = -1;
       }

       return result;
    }

    public int calculateMaximumOfMinimumsResult() {
            checkWin(tree.root);
    }


    //public boolean checkWin() {
    //    Node root = tree.getRoot();
    //    checkWin(root);
    //    return root.getScore() == 1;
    //}
    //
    //private void checkWin(Node node) {
    //    List<Node> children = node.getChildren();
    //    boolean isMaxPlayer = node.isMaxPlayer();
    //    children.forEach(child -> {
    //        if (child.getNoOfBones() == 0) {
    //            child.setScore(isMaxPlayer ? 1 : -1);
    //        } else {
    //            checkWin(child);
    //        }
    //    });
    //    Node bestChild = findBestChild(isMaxPlayer, children);
    //    node.setScore(bestChild.getScore());
    //}

}
