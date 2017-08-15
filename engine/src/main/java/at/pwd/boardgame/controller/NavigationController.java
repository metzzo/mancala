package at.pwd.boardgame.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Created by rfischer on 13/04/2017.
 */
public class NavigationController extends AnchorPane {
    public void setScreen(Parent screen) {
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
                        getChildren().add(0, screen);
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
            getChildren().add(screen);
            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(500),
                            new KeyValue(opacity, 1.0)));
            fadeIn.play();
        }
    }
}
