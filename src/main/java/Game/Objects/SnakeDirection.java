package Game.Objects;


public enum SnakeDirection {
    Top,
    Down,
    Left,
    Right;

    public static SnakeDirection get(Point start, Point end) {
        int xDiff = start.x - end.x;
        int yDiff = start.y - end.y;

        if (xDiff != 0 && yDiff != 0) {
            throw new RuntimeException("Move should be in one direction only");
        }

        if (xDiff == 1) return Left;
        if (xDiff == -1) return Right;
        if (yDiff == 1) return Top;
        if (yDiff == -1) return Down;

        throw new RuntimeException("Move distance should be only 1 unit");
    }
}
