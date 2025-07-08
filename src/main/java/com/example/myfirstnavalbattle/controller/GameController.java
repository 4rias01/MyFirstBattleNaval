package com.example.myfirstnavalbattle.controller;

import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class GameController {

    @FXML
    private GridPane gameGrid;

    public GameController() {
        gameGrid = null;
    }

    @FXML
    public void initialize() {
        gameGrid.getChildren().addAll(SetupController.getGridPaneGame());
        System.out.println("GameController initialized");
    }

    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchScene("HomeScene");
    }
}
