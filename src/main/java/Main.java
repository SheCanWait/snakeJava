import Menu.*;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Snake");
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> System.exit(0));

        var menu = new Menu(Difficulty.Normal, GameSize.Medium);

        stage.setScene(menu.getScene());
        stage.show();

        var menuThread = new Thread(menu);
        menuThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
