package Graphics;

import Game.GameState;
import Game.Objects.Point;
import Game.Objects.Snake;
import Game.Objects.SnakeDirection;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class GameGraphicsDrawer {

    private final GraphicsDrawer graphicsDrawer;
    private final Scene scene;

    double cellWidth = 50;
    double cellHeight = 50;
    double cellMargin = 1;

    double scoreBarHeight = 50;

    double gridWidth;
    double gridHeight;

    double boxBorderWidth = 10;
    double boxBorderOuterMargin = 10;
    double boxBorderTotal;
    double boxWidth;
    double boxHeight;

    double spaceBetweenSnakeParts = 1;

    double sceneWidth;
    double sceneHeight;


    public GameGraphicsDrawer(GraphicsDrawer graphicsDrawer, int gridSize) {

        boxWidth = 600;
        boxHeight = 600;

        boxBorderWidth = 10;
        boxBorderOuterMargin = 15;
        boxBorderTotal = 25;

        gridWidth = gridHeight = 550;

        scoreBarHeight = 50;

        sceneWidth = 600;
        sceneHeight = 650;

        cellHeight = cellWidth = gridWidth / gridSize;
        cellMargin = cellHeight / 20;

        this.graphicsDrawer = graphicsDrawer;
        this.scene = null;
    }

    public GameGraphicsDrawer(int gridCellsX, int gridCellsY) {

        gridWidth = cellWidth * gridCellsX;
        gridHeight = cellHeight * gridCellsY;

        boxBorderTotal = boxBorderWidth + boxBorderOuterMargin;
        boxWidth = gridWidth + boxBorderTotal * 2;
        boxHeight = gridHeight + boxBorderTotal * 2;

        sceneWidth = boxWidth;
        sceneHeight = boxHeight + scoreBarHeight;

        var root = new StackPane();
        var canvas = new Canvas(sceneWidth, sceneHeight);

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

    public void drawGame(GameState gameState) {
        graphicsDrawer.drawRectangle(GameColors.Background, 0, 0, boxWidth, boxHeight);
        graphicsDrawer.drawRectangle(GameColors.Border, boxBorderOuterMargin, boxBorderOuterMargin,
                boxWidth - boxBorderOuterMargin * 2,
                boxHeight - boxBorderOuterMargin * 2);
        graphicsDrawer.drawRectangle(GameColors.Background, boxBorderTotal, boxBorderTotal, gridWidth, gridHeight);

        var fruit = gameState.fruit;
        drawGridCell(GameColors.Fruit, fruit.x, fruit.y);

        var obstacle = gameState.obstacle;
        if (obstacle != null) {
            drawGridCell(GameColors.Obstacle, obstacle.x, obstacle.y);
        }

        var snake = gameState.snake;
        drawSnake(gameState, GameColors.Snake, snake, gameState.isOver);

        var enemySnake = gameState.enemySnake;
        drawEnemySnake(gameState, GameColors.EnemySnake, enemySnake, gameState.isOver);

        drawScoreBar(gameState.score);

        if (gameState.isOver && gameState.isWin) {
            drawWinMessage(gameState);
        }
        if (gameState.isOver && !gameState.isWin) {
            drawLoseMessage(gameState);
        }
    }

    private void drawWinMessage(GameState gameState) {
        graphicsDrawer.drawRectangle(GameColors.Fruit, 0, boxHeight, sceneWidth, scoreBarHeight);
        graphicsDrawer.drawText(GameColors.Background, 20,
                "SCORE: " + gameState.score + "      CONGRATULATIONS! YOU WON!",
                10, sceneHeight - scoreBarHeight/3);
    }

    private void drawLoseMessage(GameState gameState) {
        graphicsDrawer.drawRectangle(GameColors.DeadSnake, 0, boxHeight, sceneWidth, scoreBarHeight);
        graphicsDrawer.drawText(GameColors.Background, 20,
                "SCORE: " + gameState.score + "     Press ENTER to return to menu.",
                10, sceneHeight - scoreBarHeight/3);
    }

    private void drawScoreBar(int score) {
        graphicsDrawer.drawRectangle(GameColors.ScoreBar, 0, boxHeight, sceneWidth, scoreBarHeight);
        graphicsDrawer.drawText(GameColors.ScoreBarText, 20, "SCORE: " + score,
                10, sceneHeight - scoreBarHeight/3);
    }

    private void drawGridCell(Color color, int x, int y) {
        double calculatedX = x * cellWidth + cellMargin + boxBorderTotal;
        double calculatedY = y * cellHeight + cellMargin + boxBorderTotal;
        double calculatedCellWidth = cellWidth - 2 * cellMargin;
        double calculatedCellHeight = cellHeight - 2 * cellMargin;

        graphicsDrawer.drawRectangle(color, calculatedX, calculatedY,
                calculatedCellWidth, calculatedCellHeight);
    }

    private void drawSnake(GameState state, Color color,Snake snake, boolean isGameOver) {

        Point previousBodyPart = null;

        for (var bodyPart : snake.getBody()) {

            drawSnakePart(state, color, bodyPart, previousBodyPart, isGameOver, false);
            previousBodyPart = bodyPart;
        }
    }

    private void drawEnemySnake(GameState state, Color color,Snake snake, boolean isGameOver) {

        Point previousBodyPart = null;

        for (var bodyPart : snake.getBody()) {

            drawSnakePart(state, color, bodyPart, previousBodyPart, isGameOver, true);
            previousBodyPart = bodyPart;
        }
    }

    private void drawSnakePart(GameState state, Color color, Point bodyPart, Point previousBodyPart, boolean isGameOver, boolean isEnemy) {

        // case: bodyPart is head
        if (previousBodyPart == null) {
            Color headColor = null;
            if(isGameOver && !isEnemy && !state.isWin) {
                headColor = GameColors.DeadSnake;
            }

            if(isGameOver && isEnemy && state.isWin) {
                headColor = GameColors.DeadSnake;
            }

            if(headColor == null) {
                headColor = color;
            }


//            var headColor = isGameOver ? GameColors.DeadSnake : color;
            drawGridCell(headColor, bodyPart.x, bodyPart.y);
        }
        else {
            double calculatedX = bodyPart.x * cellWidth + cellMargin + boxBorderTotal;
            double calculatedY = bodyPart.y * cellHeight + cellMargin + boxBorderTotal;
            double calculatedCellWidth = cellWidth - 2 * cellMargin;
            double calculatedCellHeight = cellHeight - 2 * cellMargin;

            switch (determinePreviousPartDirection(bodyPart, previousBodyPart)) {

                case Top:
                    calculatedY -= cellMargin * 2 - spaceBetweenSnakeParts;
                    calculatedCellHeight += cellMargin * 2 - spaceBetweenSnakeParts;
                    break;
                case Down:
                    calculatedCellHeight += cellMargin * 2 - spaceBetweenSnakeParts;
                    break;
                case Left:
                    calculatedX -= cellMargin * 2 - spaceBetweenSnakeParts;
                    calculatedCellWidth += cellMargin * 2 - spaceBetweenSnakeParts;
                    break;
                case Right:
                    calculatedCellWidth += cellMargin * 2 - spaceBetweenSnakeParts;
                    break;
            }

            graphicsDrawer.drawRectangle(color, calculatedX, calculatedY,
                    calculatedCellWidth, calculatedCellHeight);
        }
    }

    private SnakeDirection determinePreviousPartDirection(Point bodyPart, Point previousBodyPart) {

        int xDiff = bodyPart.x - previousBodyPart.x;
        int yDiff = bodyPart.y - previousBodyPart.y;

        if (xDiff == 1) {
            return SnakeDirection.Left;
        }
        else if (xDiff == -1) {
            return SnakeDirection.Right;
        }
        else if (yDiff == 1) {
            return SnakeDirection.Top;
        }
        else if (yDiff == -1) {
            return SnakeDirection.Down;
        }

        return null;
    }
}
