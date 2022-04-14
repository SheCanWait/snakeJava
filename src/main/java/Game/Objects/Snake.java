package Game.Objects;

import java.util.LinkedList;
import java.util.List;


public class Snake {

    private LinkedList<Point> body;
    private SnakeDirection moveDirection;
    private SnakeDirection nextMoveDirection;


    public Snake () {
        body = new LinkedList<>();
        body.add(new Point());
        moveDirection = SnakeDirection.Right;
        nextMoveDirection = null;
    }


    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void setNextMoveDirection(SnakeDirection nextMoveDirection) {

        if (body.size() > 1) {

            switch (this.moveDirection) {
                case Top:
                    if (nextMoveDirection == SnakeDirection.Down)
                        return;
                    break;
                case Down:
                    if (nextMoveDirection == SnakeDirection.Top)
                        return;
                    break;
                case Left:
                    if (nextMoveDirection == SnakeDirection.Right)
                        return;
                    break;
                case Right:
                    if (nextMoveDirection == SnakeDirection.Left)
                        return;
                    break;
            }
        }

        this.nextMoveDirection = nextMoveDirection;
    }

    public void move() {

        if (nextMoveDirection == null) return;

        int headX = getHead().x;
        int headY = getHead().y;

        var last = body.removeLast();
        last.x = headX;
        last.y = headY;
        advancePoint(last);
        body.addFirst(last);

        moveDirection = nextMoveDirection;
    }

    public void moveAndExtend() {
        var lastClone = body.getLast().clone();
        body.addLast(lastClone);
        move();
    }

    public void advancePoint(Point point) {

        if (nextMoveDirection == null) return;

        switch (nextMoveDirection) {
            case Top:
                point.y--;
                break;
            case Down:
                point.y++;
                break;
            case Left:
                point.x--;
                break;
            case Right:
                point.x++;
                break;
        }
    }
}
