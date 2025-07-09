package com.example.myfirstnavalbattle.controller;

import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.model.Characters;
import com.example.myfirstnavalbattle.model.SelectCharacter;
import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class HomeController {
    @FXML
    private ImageView characterImage1;

    Characters selectedCharacter;


    @FXML
    private void initialize() {
        selectedCharacter = SelectCharacter.getSelectedCharacter();
        Image image = selectedCharacter.getImage();
        characterImage1.setImage(image);
        characterImage1.setFitHeight(120);
        characterImage1.setFitWidth(120);

    }


    @FXML
    private void handlePlay() throws IOException {
        Ship.shipsCount = 0;
        SceneManager.switchScene("SetupScene");
    }

    @FXML
    private void handleContinue(){
        System.out.println("Continue");
    }

    @FXML
    private void handleCharacter() throws IOException {
        SceneManager.switchScene("CharacterScene");
    }
}
