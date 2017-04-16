package at.pwd.boardgame.services;

import at.pwd.boardgame.controller.ControlledScreen;
import at.pwd.boardgame.controller.NavigationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rfischer on 13/04/2017.
 */

public class ScreenFactory {
    private NavigationController navigationController;
    private final HashMap<String, ScreenInfo> controllers;

    class ScreenInfo {
        String name;
        InputStream stream;
        Parent parent;
        URL location;
        OnCreatedListener createdListener;
    }

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
        this.controllers = new HashMap<>();
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public void register(String name, URL location, InputStream stream) {
        register(name, location, stream, null);
    }

    public void register(String name, URL location, InputStream stream, OnCreatedListener createdListener) {
        ScreenInfo info = new ScreenInfo();
        info.name = name;
        info.stream = stream;
        info.createdListener = createdListener;
        info.location = location;
        info.parent = null;
        controllers.put(name, info);
    }

    public Parent loadScreen(String name) {
        ScreenInfo info = controllers.get(name);

        if (info == null) {
            throw new RuntimeException("Could not find screen " + name);
        }

        if (info.parent == null) {
            try {
                FXMLLoader myLoader = new FXMLLoader();
                myLoader.setLocation(info.location);
                Parent loadScreen = myLoader.load(info.stream);
                ControlledScreen myScreenController = myLoader.getController();
                myScreenController.setNavigationController(navigationController);
                info.parent = loadScreen;

                if (info.createdListener != null) {
                    info.createdListener.created(myScreenController);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return info.parent;
    }
}
