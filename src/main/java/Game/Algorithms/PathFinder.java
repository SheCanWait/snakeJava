package Game.Algorithms;

import Game.Objects.Point;
import Game.Objects.Snake;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    public static boolean isEmpty(Point point, List<Snake> snakes, int mapSizeX, int mapSizeY) {
        if (point.y < 0 || point.y > mapSizeY - 1) return false;
        if (point.x < 0 || point.x > mapSizeX - 1) return false;

        for (Snake s : snakes)
        {
            if (collideWithSnake(point, s)){
                return false;
            }
        }

        return true;
    }

    private static boolean collideWithSnake(Point point, Snake s) {
        for (Point snakePoint : s.getBody()) {
            if (point.x == snakePoint.x && point.y == snakePoint.y) {
                return true;
            }
        }

        return false;
    }

    public static List<Point> getEmptyNeighbors(Point point, List<Snake> snakes, int mapSizeX, int mapSizeY) {
        List<Point> neighbors = new ArrayList<>();
        Point up = point.offset(0, 1);
        Point down = point.offset(0, -1);
        Point left = point.offset(-1, 0);
        Point right = point.offset(1, 0);
        if (isEmpty(up, snakes, mapSizeX, mapSizeY)) neighbors.add(up);
        if (isEmpty(down, snakes, mapSizeX, mapSizeY)) neighbors.add(down);
        if (isEmpty(left, snakes, mapSizeX, mapSizeY)) neighbors.add(left);
        if (isEmpty(right, snakes, mapSizeX, mapSizeY)) neighbors.add(right);
        return neighbors;
    }

    public static List<Point> getPath(Point start, Point end, List<Snake> snakes, int mapSizeX, int mapSizeY) {
        boolean finished = false;
        List<Point> used = new ArrayList<>();
        used.add(start);
        while (!finished) {
            List<Point> newOpen = new ArrayList<>();
            for(int i = 0; i < used.size(); ++i){
                Point point = used.get(i);
                for (Point neighbor : getEmptyNeighbors(point, snakes, mapSizeX, mapSizeY)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for(Point point : newOpen) {
                used.add(point);
                if (end.equals(point)) {
                    finished = true;
                    break;
                }
            }

            if (!finished && newOpen.isEmpty())
                return null;
        }

        List<Point> path = new ArrayList<>();
        Point point = used.get(used.size() - 1);
        while(point.previous != null) {
            path.add(0, point);
            point = point.previous;
        }
        return path;
    }
}