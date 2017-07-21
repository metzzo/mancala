package at.pwd.boardgame.controller.mancala;

import at.pwd.boardgame.controller.AddAgentController;
import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.controller.ControlledScreen;
import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.game.GameFactory;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.agent.AgentService;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public class MancalaSetUpController implements ControlledScreen, Initializable {
    public static final String SETUP_SCREEN = "/setup_controller.fxml";

    private NavigationController navigationController;
    private ListProperty<Agent> agents = new SimpleListProperty<>();

    @FXML
    ComboBox<Agent> player1Agent;
    @FXML
    ComboBox<Agent> player2Agent;
    @FXML
    Spinner computationTime;

    public static Parent createSetUpScreen() {
        return ScreenFactory.getInstance().loadScreen(
                MancalaSetUpController.class.getResource(SETUP_SCREEN),
                MancalaSetUpController.class.getResourceAsStream(SETUP_SCREEN),
                null
        );
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Agent> list = AgentService.getInstance().getAgents();
        if (list.size() == 0) {
            throw new RuntimeException("There is no agent!");
        }

        agents.set(FXCollections.observableArrayList(list));
        player1Agent.setItems(agents);
        player1Agent.setValue(agents.get(0));

        player2Agent.setItems(agents);
        player2Agent.setValue(agents.get(0));
    }

    @FXML
    public void startGamePressed(ActionEvent actionEvent) {
        final MancalaGame game = (MancalaGame) GameFactory.getInstance().create(MancalaGame.GAME_NAME);

        Parent screen = ScreenFactory.getInstance().loadScreen(
                BoardController.class.getResource(MancalaBoardController.GAME_SCREEN),
                game.getViewXml(),
                s -> {
                    List<Agent> selectedAgents = new ArrayList<>();
                    selectedAgents.add(player1Agent.getValue());
                    selectedAgents.add(player2Agent.getValue());

                    BoardController ctrl = (BoardController) s;
                    ctrl.setGame(game);
                    ctrl.setAgents(selectedAgents);
                    ctrl.setComputationTime((Integer)computationTime.getValue());
                    ctrl.start();
                });

        navigationController.setScreen(screen);
    }

    public void player1loadAi(ActionEvent actionEvent) {
        loadAi();
    }

    public void player2loadAi(ActionEvent actionEvent) {
        loadAi();
    }

    private void loadAi() {
        AddAgentController.show();


        /*FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose jar file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Jar files (*.jar)", "*.jar");
        fileChooser.setSelectedExtensionFilter(extFilter);
        File jar = fileChooser.showOpenDialog(navigationController.getScene().getWindow());

        if (jar != null) {

        }*/
    }
}
