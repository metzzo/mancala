package at.pwd.boardgame.controller;

import at.pwd.boardgame.Main;
import at.pwd.boardgame.services.*;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.mancala.MancalaGame;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Set up a new MancalaGame
 */
public class SetUpController implements ScreenFactory.BoardGameScreen, Initializable {
    private static final String SETUP_SCREEN = "/setup_controller.fxml";
    private static final String BOARD_GENERATOR_TRANSFORMER = "/board_generator.xsl";
    private static final String GAME_SCREEN = "/board_controller.fxml";
    private static final File CONFIG_FILE = new File("config.xml");
    static final String PWD_DOMAIN = "http://programming-with-design.at/";

    private NavigationController navigationController;
    private ListProperty<Agent> agents = new SimpleListProperty<>();
    private ConfigService config;


    @FXML
    ComboBox<Agent> player1Agent;
    @FXML
    ComboBox<Agent> player2Agent;
    @FXML
    /*Fidget*/Spinner<Integer> computationTime;
    @FXML
    /*Fidget*/Spinner<Integer> stonesPerSlot;
    @FXML
    /*Fidget*/Spinner<Integer> slotsPerPlayer;
    @FXML
    Hyperlink website;
    @FXML
    Hyperlink details;

    /**
     * @return Creates a new instance of the set up screen
     */
    public static Parent createSetUpScreen() {
        return ScreenFactory.getInstance().loadScreen(
                SetUpController.class.getResource(SETUP_SCREEN)
        );
    }

    /**
     * Constructor, loading the configuration
     */
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

        fixSpinner(computationTime);
        bindSpinner(
                computationTime,
                config.getComputationTime(),
                (observable, oldValue, newValue) -> config.setComputationTime(newValue.intValue())
        );

        fixSpinner(slotsPerPlayer);
        bindSpinner(
                slotsPerPlayer,
                config.getSlotsPerPlayer(),
                (observable, oldValue, newValue) -> config.setSlotsPerPlayer(newValue.intValue())
        );

        fixSpinner(stonesPerSlot);
        bindSpinner(
                stonesPerSlot,
                config.getStonesPerSlot(),
                (observable, oldValue, newValue) -> config.setStonesPerSlot(newValue.intValue())
        );

        website.setOnAction(event -> Main.getApp().getHostServices().showDocument(PWD_DOMAIN));
        details.setOnAction(event -> AboutController.show());
    }

    private void bindSpinner(Spinner<Integer> spinner, int initialValue, ChangeListener<Number> listener) {
        ObjectProperty<Integer> objectProp = new SimpleObjectProperty<>(initialValue);
        IntegerProperty spinnerProperty = IntegerProperty.integerProperty(objectProp);
        spinnerProperty.addListener(listener);
        spinner.getValueFactory().valueProperty().bindBidirectional(objectProp);
    }

    /**
     * Spinner in JavaFX do not commit on focus lost => this method fixes this issue
     * @param spinner
     */
    private void fixSpinner(Spinner<Integer> spinner) {
        SpinnerValueFactory<Integer> factory = spinner.getValueFactory();
        TextFormatter<Integer> formatter = new TextFormatter<>(factory.getConverter(), factory.getValue());
        spinner.getEditor().setTextFormatter(formatter);
        factory.valueProperty().bindBidirectional(formatter.valueProperty());
    }

    /**
     * Starts the game: generates the board, initializes the BoardController and transitions
     * @param actionEvent the event that caused it
     */
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
                    ctrl.setComputationTime(computationTime.getValue());
                    ctrl.start();
                });

        navigationController.setScreen(screen);
    }

    /**
     * Loads a new agent for player 1
     * @param actionEvent The event that caused the event
     */
    public void player1loadAi(ActionEvent actionEvent) {
        loadAi(player1Agent);
    }

    /**
     * Loads a new agent for player 2
     * @param actionEvent The event that caused the event
     */
    public void player2loadAi(ActionEvent actionEvent) {
        loadAi(player2Agent);
    }

    private void loadAi(ComboBox<Agent> targetCombobox) {
        int oldSize = agents.size();

        AddAgentController.show();
        reloadAgents();

        if (agents.size() != oldSize) {
            targetCombobox.setValue(agents.get(agents.size() - 1));
        }
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
