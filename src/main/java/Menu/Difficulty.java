package Menu;

public enum Difficulty {
    Easy,
    Normal,
    Hard;

    public static Difficulty fromInt(int val) {

        switch (val) {
            case 0:
                return Easy;
            case 1:
                return Normal;
            case 2:
                return Hard;
        }

        return Normal;
    }
}
