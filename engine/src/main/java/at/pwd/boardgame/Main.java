package at.pwd.boardgame;

import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.controller.SetUpController;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.services.AgentService;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by rfischer on 13/04/2017.
 */
public class Main extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        primaryStage.setTitle("Mancala Board Game");

        MancalaGame.init();

        NavigationController ctrl = ScreenFactory.getInstance()
                .getNavigationController();

        ctrl.setScreen(SetUpController.createSetUpScreen());

        Scene scene = new Scene(ctrl);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        for (String arg : args) {
            Agent a = null;
            try {
                a = (Agent) Class.forName(arg).newInstance();
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                e.printStackTrace();
            }
            if (a != null) {
                AgentService.getInstance().register(a);
            }
        }
        launch(args);
    }
}
