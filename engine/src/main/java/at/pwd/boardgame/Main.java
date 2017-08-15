package at.pwd.boardgame;

import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.controller.SetUpController;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.services.AgentService;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by rfischer on 13/04/2017.
 */
public class Main extends Application {
    private static Application app;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.app = this;

        primaryStage.setTitle("Mancala Boardgame Engine");
        primaryStage.getIcons().add(new Image("logo.png"));

        MancalaGame.init();

        NavigationController ctrl = ScreenFactory.getInstance()
                .getNavigationController();

        ctrl.setScreen(SetUpController.createSetUpScreen());

        Scene scene = new Scene(ctrl);
        primaryStage.setMinWidth(400.0f);
        primaryStage.setMinHeight(440.0f);
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

    public static Application getApp() {
        return app;
    }
}
