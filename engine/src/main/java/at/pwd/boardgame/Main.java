package at.pwd.boardgame;

import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.controller.mancala.MancalaSetUpController;
import at.pwd.boardgame.game.mancala.MancalaGame;
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
        primaryStage.setTitle("Mancala Board Game");

        MancalaGame.init();

        NavigationController mainContainer = new NavigationController();
        ScreenFactory.getInstance().setNavigationController(mainContainer);
        mainContainer.setScreen(MancalaSetUpController.createSetUpScreen());

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
