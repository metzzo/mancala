package at.pwd.boardgame.ui;

import at.pwd.boardgame.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rfischer on 13/04/2017.
 */
public class NavigationController extends StackPane {
    private Map<String, Node> screens = new HashMap<>();

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public boolean loadScreen(String name) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(name + ".fxml"));
            Parent loadScreen = myLoader.load();
            ControlledScreen myScreenControler = myLoader.getController();
            myScreenControler.setNavigationController(this);
            addScreen(name, loadScreen);
            return true;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setScreen(final String name) {
        if (screens.get(name) == null) {
            this.loadScreen(name); // load screen if necessary
        }

        final DoubleProperty opacity = opacityProperty();

        // Is there is more than one screen
        if (!getChildren().isEmpty()) {
            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(opacity, 1.0)),
                    new KeyFrame(new Duration(400), t -> {
                        // remove displayed screen
                        getChildren().remove(0);
                        // add new screen
                        getChildren().add(0, screens.get(name));
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO,
                                        new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(300),
                                        new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }, new KeyValue(opacity, 0.0)));
            fade.play();
        } else {
            // no one else been displayed, then just show
            setOpacity(0.0);
            getChildren().add(screens.get(name));
            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(500),
                            new KeyValue(opacity, 1.0)));
            fadeIn.play();
        }
    }
}
