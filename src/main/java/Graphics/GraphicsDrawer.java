package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class GraphicsDrawer {

    private GraphicsContext graphicsContext;


    public GraphicsDrawer(GraphicsContext graphicsContext) {

        this.graphicsContext = graphicsContext;
    }


    public void drawRectangle(Color color, double x, double y, double width, double height) {

        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, width, height);
    }

    public void drawText(Color color, double fontSize, String text, double x, double y) {

        drawText(color, fontSize, text, x, y, "Consolas");
    }

    public void drawText(Color color, double fontSize, String text, double x, double y, String fontName) {

        graphicsContext.setFill(color);
        graphicsContext.setFont(Font.font(fontName, fontSize));
        graphicsContext.fillText(text, x, y);
    }
}
