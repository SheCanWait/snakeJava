package Graphics;

import Menu.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class MenuDrawer {

    private GraphicsDrawer graphicsDrawer;
    private Scene scene;

    double windowWidth = 600;
    double windowHeight = 650;

    double menuWidth = 350;
    double menuHeight = 250;

    double menuHorizontalOffset;
    double menuVerticalOffset;

    int menuTextRows = 3;
    int menuTextColumns = 2;
    double gridCellMargin = 10;

    double textMargin = 10;
    double textRowHeight;
    double textColumnWidth;

    static final String[][] MenuOptions = new String[][] {
            { "small", "medium", "big" },
            { "easy", "normal", "hard" }
    };


    public MenuDrawer() {

        menuHorizontalOffset = (windowWidth - menuWidth) / 2;
        menuVerticalOffset = (windowHeight - menuHeight) / 5 * 3;

        textRowHeight = menuHeight / menuTextRows;
        textColumnWidth = menuWidth / menuTextColumns;

        var root = new StackPane();
        var canvas = new Canvas(windowWidth, windowHeight);

        canvas.setFocusTraversable(true);
        root.getChildren().add(canvas);

        var context = canvas.getGraphicsContext2D();

        graphicsDrawer = new GraphicsDrawer(context);
        scene = new Scene(root);
    }


    public Scene getScene() {

        return scene;
    }

    public void setOnKeyPressed(EventHandler<? super KeyEvent> handler) {

        scene.setOnKeyPressed(handler);
    }

    public void drawMenu(Menu menu) {

        graphicsDrawer.drawRectangle(MenuColors.WindowBackground, 0, 0, windowWidth, windowHeight);
        graphicsDrawer.drawRectangle(MenuColors.MenuBackground, menuHorizontalOffset - gridCellMargin, menuVerticalOffset - gridCellMargin,
                menuWidth + gridCellMargin * 2, menuHeight + gridCellMargin * 2);

        drawInstructions();
        drawOptionsGrid(menu);

        graphicsDrawer.drawText(MenuColors.Message, 20, "Press ESC to exit",
                menuHorizontalOffset + 80, menuVerticalOffset + menuHeight + 80);
    }

    private void drawInstructions() {

        graphicsDrawer.drawText(MenuColors.Message, 35, "Use arrows to choose",
                menuHorizontalOffset + 21, menuVerticalOffset - 100, "Impact");
        graphicsDrawer.drawText(MenuColors.Message, 35, "and press ENTER to start",
                menuHorizontalOffset + 5, menuVerticalOffset - 50, "Impact");
    }

    private void drawOptionsGrid(Menu menu) {

        for (int x = 0; x < menuTextColumns; x++) {

            for (int y = 0; y < menuTextRows; y++) {

                Color backgroundColor;
                Color textColor;

                if (x == menu.activeColumn) {

                    backgroundColor = MenuColors.ActiveOptionBackground;
                    textColor = MenuColors.ActiveOptionText;
                }
                else {

                    backgroundColor = MenuColors.InactiveOptionBackground;
                    textColor = MenuColors.InactiveOptionText;
                }

                if (x == 0 && y == menu.gameSize.ordinal() ||
                    x == 1 && y == menu.difficulty.ordinal()) {

                    backgroundColor = MenuColors.ChosenOptionBackground;
                    textColor = MenuColors.ChosenOptionText;
                }

                double xPos = menuHorizontalOffset + (x * textColumnWidth) + gridCellMargin;
                double width = textColumnWidth - (2 * gridCellMargin);

                double yPos = menuVerticalOffset + (y * textRowHeight) + gridCellMargin;
                double height = textRowHeight - (2 * gridCellMargin);

                graphicsDrawer.drawRectangle(backgroundColor, xPos, yPos, width, height);
                graphicsDrawer.drawText(textColor, 30, MenuOptions[x][y],
                        menuHorizontalOffset + (textColumnWidth * x) + gridCellMargin + textMargin,
                        menuVerticalOffset + (textRowHeight * y) + (textRowHeight / 5 * 2)
                                + gridCellMargin + textMargin);
            }
        }
    }

    public GraphicsDrawer getGraphicsDrawer() {

        return graphicsDrawer;
    }
}
