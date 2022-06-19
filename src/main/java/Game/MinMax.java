package Game;

import Game.Objects.Node;
import Game.Objects.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MinMax {
    int x;
    int y;
//    public static final int MAX_GAME_DEPTH = 16^3;
//    public static int gameDepth = 0;

    private List<Node> calculateChildNodes(Node node) {
        List<Point> possibleSnakeHeadPositions = calculatePossibleSnakePositions(node.snakeBody);
        List<Point> possibleEnemySnakeHeadPositions = calculatePossibleSnakePositions(node.enemySnakeBody);

        List<Node> childNodes = new ArrayList<>();
        for(int i = 0 ; i < possibleSnakeHeadPositions.size() ; i++) {
            for(int j = 0 ; j < possibleEnemySnakeHeadPositions.size() ; j++) {
                Node childNode = new Node();
                childNode.fruit = node.fruit;

                LinkedList<Point> snakeBodyCopy = (LinkedList<Point>)(node.snakeBody.clone());
                if(!possibleSnakeHeadPositions.get(i).equals(childNode.fruit)) {
                    snakeBodyCopy.removeLast();
                }
                snakeBodyCopy.addFirst(possibleSnakeHeadPositions.get(i));
                childNode.snakeBody = snakeBodyCopy;

                LinkedList<Point> enemySnakeBodyCopy = (LinkedList<Point>)(node.enemySnakeBody.clone());
                if(!possibleEnemySnakeHeadPositions.get(j).equals(childNode.fruit)) {
                    enemySnakeBodyCopy.removeLast();
                }
                enemySnakeBodyCopy.addFirst(possibleEnemySnakeHeadPositions.get(j));
                childNode.enemySnakeBody = enemySnakeBodyCopy;

                childNodes.add(childNode);
            }
        }
        return childNodes;
    }

    private List<Point> calculatePossibleSnakePositions(LinkedList<Point> snakeBody) {
        Point snakeHead = snakeBody.getFirst();
        List<Point> snakePossibleNextHeadPositions = new LinkedList<>(Arrays.asList(
                new Point(snakeHead.x + 1, snakeHead.y),
                new Point(snakeHead.x - 1, snakeHead.y),
                new Point(snakeHead.x, snakeHead.y + 1),
                new Point(snakeHead.x, snakeHead.y - 1)));
        if(snakeBody.size() > 1) {
            Point secondSnakeElement = snakeBody.get(1);
            snakePossibleNextHeadPositions.remove(secondSnakeElement);
        }

        snakePossibleNextHeadPositions.removeIf(point ->
                point.x < 0 ||
                point.y < 0 ||
                point.x >= this.x ||
                point.y >= this.y);

        return snakePossibleNextHeadPositions;
    }

    public Node constructTree(GameState state, int x, int y, int minMaxDepth) {
        this.x = x;
        this.y = y;
        Node root = new Node();
        root.snakeBody = state.snake.getBody();
        root.enemySnakeBody = state.enemySnake.getBody();
        root.fruit = state.fruit;

        constructTree(root, minMaxDepth, 0);
        return root;
    }

    private void constructTree(Node parentNode, int minMaxDepth, int currentDepth) {
        if (currentDepth < minMaxDepth) {
            List<Node> childNodes = calculateChildNodes(parentNode);
            parentNode.childNodes.addAll(childNodes);

            parentNode.childNodes.forEach(childNode -> {
                int simulatedGameResult = simulateGameResult(childNode);

                if (simulatedGameResult == Integer.MAX_VALUE) {
                    simulateGameResult(childNode);
                }

                childNode.gameResult = simulatedGameResult;
                if(simulatedGameResult == 0 || simulatedGameResult == -1 || simulatedGameResult == 1) {
                    constructTree(childNode, minMaxDepth, currentDepth + 1);
                }
            });
        }
    }

    // -1 -> enemy loses
    // 0 -> game not ended
    // 1 -> enemy wins

    // Integer.MIN_VALUE -> we win
    // -1 -> we get fruit
    // 0 -> game not ended
    // 1 -> AI gets fruit
    // Integer.MAX_VALUE -> AI wins
    private int simulateGameResult(Node node) {
        if(node.snakeBody.getFirst().equals(node.fruit)) {
            return -1;
        }
        if(node.enemySnakeBody.getFirst().equals(node.fruit)) {
            return 1;
        }
        if(GameState.doesCollideWithAnything(node.snakeBody.getFirst(), node.snakeBody.stream().skip(1).collect(Collectors.toList()), node.enemySnakeBody, false, x, y)) { // TODO idk if true / false should be inverted here
            return Integer.MAX_VALUE;
        }
        if(GameState.doesCollideWithAnything(node.enemySnakeBody.getFirst(), node.snakeBody.stream().skip(1).collect(Collectors.toList()), node.enemySnakeBody,true, x, y)) {
            return Integer.MIN_VALUE;
        }
        return 0;
    }

}
