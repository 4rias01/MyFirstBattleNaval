package com.example.myfirstnavalbattle.controller;

import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;

import java.io.IOException;

public class GameController {

    public GameController() {}

    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchScene("HomeScene");
    }
}
