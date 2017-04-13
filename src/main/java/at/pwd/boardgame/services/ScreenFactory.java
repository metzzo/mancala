package at.pwd.boardgame.services;

import at.pwd.boardgame.controller.ControlledScreen;
import at.pwd.boardgame.controller.NavigationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.InputStream;
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

    public void register(String name, InputStream stream) {
        ScreenInfo info = new ScreenInfo();
        info.name = name;
        info.stream = stream;
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
                Parent loadScreen = myLoader.load(info.stream);
                ControlledScreen myScreenControler = myLoader.getController();
                myScreenControler.setNavigationController(navigationController);
                info.parent = loadScreen;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return info.parent;
    }
}
