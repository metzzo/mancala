package at.pwd.boardgame.controller;

import at.pwd.boardgame.services.*;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.mancala.MancalaGame;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by rfischer on 13/04/2017.
 */
public class SetUpController implements ControlledScreen, Initializable {
    private static final String SETUP_SCREEN = "/setup_controller.fxml";
    private static final String BOARD_GENERATOR_TRANSFORMER = "/board_generator.xsl";
    private static final String GAME_SCREEN = "/board_controller.fxml";
    private static final File CONFIG_FILE = new File("config.xml");

    private NavigationController navigationController;
    private ListProperty<Agent> agents = new SimpleListProperty<>();
    private ConfigService config;

    private IntegerProperty computationTimeProperty = new SimpleIntegerProperty();

    @FXML
    ComboBox<Agent> player1Agent;
    @FXML
    ComboBox<Agent> player2Agent;
    @FXML
    Spinner<Integer> computationTime;
    @FXML
    Spinner<Integer> stonesPerSlot;
    @FXML
    Spinner<Integer> slotsPerPlayer;

    public static Parent createSetUpScreen() {
        return ScreenFactory.getInstance().loadScreen(
                SetUpController.class.getResource(SETUP_SCREEN),
                SetUpController.class.getResourceAsStream(SETUP_SCREEN),
                null
        );
    }

    public SetUpController() {
        config = ConfigService.getInstance().load(CONFIG_FILE);
        config.save();
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadAgents();

        player1Agent.setItems(agents);
        player1Agent.setValue(agents.get(0));

        player2Agent.setItems(agents);
        player2Agent.setValue(agents.get(0));


        bindSpinner(
                computationTime,
                config.getComputationTime(),
                (observable, oldValue, newValue) -> config.setComputationTime(newValue.intValue())
        );

        bindSpinner(
                slotsPerPlayer,
                config.getSlotsPerPlayer(),
                (observable, oldValue, newValue) -> config.setSlotsPerPlayer(newValue.intValue())
        );

        bindSpinner(
                stonesPerSlot,
                config.getStonesPerSlot(),
                (observable, oldValue, newValue) -> config.setStonesPerSlot(newValue.intValue())
        );
    }

    private void bindSpinner(Spinner<Integer> spinner, int initialValue, ChangeListener<Number> listener) {
        ObjectProperty<Integer> objectProp = new SimpleObjectProperty<>(initialValue);
        IntegerProperty spinnerProperty = IntegerProperty.integerProperty(objectProp);
        spinnerProperty.addListener(listener);
        spinner.getValueFactory().valueProperty().bindBidirectional(objectProp);
    }

    @FXML
    public void startGamePressed(ActionEvent actionEvent) {
        InputStream board = generateBoard();
        final MancalaGame game = (MancalaGame) GameFactory.getInstance().create(MancalaGame.GAME_NAME, board);

        Parent screen = ScreenFactory.getInstance().loadScreen(
                BoardController.class.getResource(SetUpController.GAME_SCREEN),
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
        player1Agent.setValue(agents.get(agents.size() - 1));
    }

    public void player2loadAi(ActionEvent actionEvent) {
        loadAi();
        player2Agent.setValue(agents.get(agents.size() - 1));
    }

    private void loadAi() {
        AddAgentController.show();

        reloadAgents();
    }

    private void reloadAgents() {
        List<Agent> list = AgentService.getInstance().getAgents();
        if (list.size() == 0) {
            throw new RuntimeException("There is no agent!");
        }

        agents.set(FXCollections.observableArrayList(list));
    }

    private InputStream generateBoard() {
        Map<String, String> params = new HashMap<>();
        params.put("num_stones", stonesPerSlot.getValue().toString());
        params.put("slots_per_player", slotsPerPlayer.getValue().toString());
        params.put("computation_time", computationTime.getValue().toString());

        return XSLTService.getInstance().execute(
                BOARD_GENERATOR_TRANSFORMER,
                new StreamSource(new StringReader("<empty/>")),
                params
        );
    }
}
