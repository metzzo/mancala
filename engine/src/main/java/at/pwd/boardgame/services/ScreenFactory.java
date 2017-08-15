package at.pwd.boardgame.services;

import at.pwd.boardgame.controller.ControlledScreen;
import at.pwd.boardgame.controller.NavigationController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by rfischer on 13/04/2017.
 */

public class ScreenFactory {
    private NavigationController navigationController;

    public interface OnCreatedListener {
        void created(ControlledScreen screen);
    }

    private static ScreenFactory ourInstance;

    public static ScreenFactory getInstance() {
        if (ourInstance == null) {
            ourInstance = new ScreenFactory();
        }
        return ourInstance;
    }


    private ScreenFactory() {

    }

    public NavigationController getNavigationController() {
        if (navigationController == null) {
            navigationController = new NavigationController();
        }
        return navigationController;
    }

    public Parent loadScreen(URL location, InputStream stream, OnCreatedListener createdListener) {
        try {
            FXMLLoader myLoader = new FXMLLoader();
            myLoader.setLocation(location);
            Parent loadScreen = myLoader.load(stream);
            ControlledScreen myScreenController = myLoader.getController();
            myScreenController.setNavigationController(navigationController);

            if (createdListener != null) {
                createdListener.created(myScreenController);
            }

            return loadScreen;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
