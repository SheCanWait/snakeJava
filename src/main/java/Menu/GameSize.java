package Menu;

public enum GameSize {
    Small,
    Medium,
    Big;

    public static GameSize fromInt(int val) {

        switch (val) {
            case 0:
                return Small;
            case 1:
                return Medium;
            case 2:
                return Big;
        }

        return Medium;
    }
}
