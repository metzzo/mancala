package at.pwd.boardgame;

import at.pwd.boardgame.ui.NavigationController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by rfischer on 13/04/2017.
 */
public class Main extends Application {
    public static final String SETUP_SCREEN = "/setup_controller";

    public static final String GAME_SCREEN = "/board_controller";


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mancala Boardgame Engine");

        NavigationController mainContainer = new NavigationController();

        // preload screens
        mainContainer.loadScreen(Main.SETUP_SCREEN);
        mainContainer.loadScreen(Main.GAME_SCREEN);

        // TODO: setup screen with option for game screen
        mainContainer.setScreen(Main.SETUP_SCREEN);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
