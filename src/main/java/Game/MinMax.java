package Game;

import Game.Objects.Node;
import Game.Objects.Point;
import Game.Objects.Tree;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

import static Game.Objects.SnakeDirection.*;

public class MinMax {
    Tree tree;

    static List<Node> calculateChildNodes(Node node) {
        List<Pair<Point, Point>> possibleMoveDirections = Arrays.asList(
                new Pair(Top, Top), new Pair(Top, Down), new Pair(Top, Right), new Pair(Top, Left),
                new Pair(Down, Top), new Pair(Down, Down), new Pair(Down, Right), new Pair(Down, Left),
                new Pair(Right, Top), new Pair(Right, Down), new Pair(Right, Right), new Pair(Right, Left),
                new Pair(Left, Top), new Pair(Left, Down), new Pair(Left, Right), new Pair(Left, Left)
        );

        possibleMoveDirections.forEach(pair -> {
            // TODO here
//            node.snakeBody.
        });
    }

    //    public void move() {
    //
    //        if (nextMoveDirection == null) return;
    //
    //        int headX = getHead().x;
    //        int headY = getHead().y;
    //
    //        var last = body.removeLast();
    //        last.x = headX;
    //        last.y = headY;
    //        advancePoint(last);
    //        body.addFirst(last);
    //
    //        moveDirection = nextMoveDirection;
    //    }

    public void constructTree(GameState state) {
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
        List<GameState> listOfPossibleStates = calculateChildNodes(parentNode);

        listOfPossibleStates.forEach(possibleState -> {

        });


        boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
        listofPossibleHeaps.forEach(n -> {
            Node newNode = new Node(n, isChildMaxPlayer);
            parentNode.addChild(newNode);
            if (newNode.getNoOfBones() > 0) {
                constructTree(newNode);
            }
        });
    }




}
