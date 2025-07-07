package com.example.myfirstnavalbattle.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage mainStage;

    public static void setStage(Stage stage) { mainStage = stage; }

    public static void switchScene(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/com/example/myfirstnavalbattle/"+ sceneName + "View/" + sceneName + ".fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        AnimationsManager.applyGlobalCursor(scene);
        AnimationsManager.applyToAllButtons(root);

        mainStage.setScene(scene);
        mainStage.setFullScreen(false);
    }
}
