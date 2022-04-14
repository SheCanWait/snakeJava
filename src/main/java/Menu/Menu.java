package Menu;

import Game.Game;
import Graphics.MenuDrawer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Menu implements Runnable {

    private MenuDrawer menuDrawer;

    public Difficulty difficulty;
    public GameSize gameSize;
    public int activeColumn;

    private boolean exit = false;
    private boolean startGame = false;

    public Menu(Difficulty difficulty, GameSize gameSize) {

        this.difficulty = difficulty;
        this.gameSize = gameSize;
        this.activeColumn = 0;

        menuDrawer = new MenuDrawer();
        menuDrawer.setOnKeyPressed(this::onKeyPressed);
    }


    public Scene getScene() {

        return menuDrawer.getScene();
    }

    private void onKeyPressed(KeyEvent e) {

        var keyCode = e.getCode();

        switch (keyCode) {
            case UP:
            case DOWN:
                handleUpDown(keyCode);
                break;
            case LEFT:
                if (activeColumn == 1) {
                    activeColumn = 0;
                }
                break;
            case RIGHT:
                if (activeColumn == 0) {
                    activeColumn = 1;
                }
                break;
            case ENTER:
                startGame = true;
                break;
            case ESCAPE:
                exit = true;
                break;
        }

        menuDrawer.drawMenu(this);
    }

    private void handleUpDown(KeyCode keyCode) {

        if (activeColumn == 0) {

            int gameSizeInt = gameSize.ordinal();

            if (keyCode == KeyCode.UP && gameSizeInt > 0) {
                gameSize = GameSize.fromInt(gameSizeInt - 1);
            }
            else if (keyCode == KeyCode.DOWN && gameSizeInt < 2) {
                gameSize = GameSize.fromInt(gameSizeInt + 1);
            }
        }
        else if (activeColumn == 1) {

            int difficultyInt = difficulty.ordinal();

            if (keyCode == KeyCode.UP && difficultyInt > 0) {
                difficulty = Difficulty.fromInt(difficultyInt - 1);
            }
            else if (keyCode == KeyCode.DOWN && difficultyInt < 2) {
                difficulty = Difficulty.fromInt(difficultyInt + 1);
            }
        }
    }

    @Override
    public void run() {

        menuDrawer.drawMenu(this);

        while (!exit) {

            try {
                Thread.sleep(100);
            }
            catch (Exception sd) {
                System.out.println(sd.getMessage());
            }

            if (startGame) {

                int gridSize = getGridSize();
                int frameRate = getFrameRate();

                var game = new Game(gridSize, frameRate, menuDrawer.getGraphicsDrawer());
                getScene().setOnKeyPressed(game::onKeyPressed);
                game.start();

                startGame = false;
                getScene().setOnKeyPressed(this::onKeyPressed);

                menuDrawer.drawMenu(this);
            }
        }

        System.exit(0);
    }

    private int getGridSize() {

        switch (gameSize) {

            case Small:
                return 7;
            case Medium:
                return 11;
            case Big:
                return 17;
            default:
                return 0;
        }
    }

    private int getFrameRate() {

        switch (difficulty) {

            case Easy:
                return 2;
            case Normal:
                return 4;
            case Hard:
                return 6;
            default:
                return 0;
        }
    }
}
