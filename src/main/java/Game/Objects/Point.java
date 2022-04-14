package Game.Objects;


public class Point {

    public int x;
    public int y;


    public Point() { }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void setLocation (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point other) {
        return (x == other.x && y == other.y);
    }

    public Point clone() {
        return new Point(x, y);
    }
}
