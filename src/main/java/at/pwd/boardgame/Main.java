package at.pwd.boardgame;

import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.controller.SetUpController;
import at.pwd.boardgame.game.mancala.MancalaBoardController;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by rfischer on 13/04/2017.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MancalaGame Boardgame Engine");

        NavigationController mainContainer = new NavigationController();
        ScreenFactory.getInstance().setNavigationController(mainContainer);

        MancalaBoardController.init();
        SetUpController.init();

        // TODO: setup screen with option for game screen
        mainContainer.setScreen(MancalaBoardController.GAME_SCREEN);

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
