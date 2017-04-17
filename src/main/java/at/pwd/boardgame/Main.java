package at.pwd.boardgame;

import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.controller.SetUpController;
import at.pwd.boardgame.game.interfaces.Agent;
import at.pwd.boardgame.game.GameFactory;
import at.pwd.boardgame.game.interfaces.Game;
import at.pwd.boardgame.game.mancala.MancalaHumanAgent;
import at.pwd.boardgame.services.ControllerFactory;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rfischer on 13/04/2017.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MancalaGame Boardgame Engine");

        Game game = GameFactory.getInstance().create("normal_mancala");

        List<Agent> agents = new ArrayList<>();
        agents.add(new MancalaHumanAgent());
        agents.add(new MancalaHumanAgent());

        NavigationController mainContainer = new NavigationController();
        ControllerFactory.getInstance().setNavigationController(mainContainer);

        BoardController.init(game, agents);
        SetUpController.init();

        // TODO: setup screen with option for game screen
        mainContainer.setScreen(BoardController.GAME_SCREEN);

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
