package at.pwd.boardgame.services;

import at.pwd.boardgame.controller.NavigationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Service for creating screens.
 */
public class ScreenFactory {
    private NavigationController navigationController;

    /**
     * Interface that is called when a screen is successfully created
     */
    public interface OnCreatedListener {
        /**
         * the method that is being called
         * @param screen with the created screen
         */
        void created(BoardGameScreen screen);
    }

    private static ScreenFactory ourInstance;

    /**
     * @return Returns the Singleton ScreenFactory instance
     */
    public static ScreenFactory getInstance() {
        if (ourInstance == null) {
            ourInstance = new ScreenFactory();
        }
        return ourInstance;
    }


    private ScreenFactory() { }

    /**
     * @return Returns the NavigationController of this screenfactory
     */
    public NavigationController getNavigationController() {
        if (navigationController == null) {
            navigationController = new NavigationController();
        }
        return navigationController;
    }

    /**
     * Loads a given screen
     *
     * @param location The URL of the FXML file
     * @param stream The input stream containing the FXML file
     * @param createdListener The listener that is called when the creation finishes
     * @return the loaded screen
     */
    public Parent loadScreen(URL location, InputStream stream, OnCreatedListener createdListener) {
        try {
            FXMLLoader myLoader = new FXMLLoader();
            myLoader.setLocation(location);
            Parent loadScreen = myLoader.load(stream);
            BoardGameScreen myScreenController = myLoader.getController();
            myScreenController.setNavigationController(navigationController);

            if (createdListener != null) {
                createdListener.created(myScreenController);
            }

            return loadScreen;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience method for loading a screen only by URL
     * @param location the given location
     * @return the loaded screen
     */
    public Parent loadScreen(URL location) {
        try {
            return loadScreen(location, location.openStream(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Interface for a Screen that is shown by ScreenFactory
     */
    public interface BoardGameScreen {
        void setNavigationController(NavigationController navigationController);
    }
}
