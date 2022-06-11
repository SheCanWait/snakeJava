package Game.Objects;


public class Point {

    public int x;
    public int y;
    public Point previous;


    public Point() { }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, Point prev) {
        this.x = x;
        this.y = y;
        this.previous = prev;
    }


    public void setLocation (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point clone() {
        return new Point(x, y);
    }

    public Point offset(int offsetX, int offsetY) {
        return new Point(x + offsetX, y + offsetY, this);
    }

    @Override
    public String toString() { return String.format("(%d, %d)", x, y); }

    @Override
    public boolean equals(Object o) {
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}
