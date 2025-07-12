package com.example.myfirstnavalbattle.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Scene mainScene;

    public static void setStage(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(
                "/com/example/myfirstnavalbattle/scenes/HomeSceneView/HomeScene.fxml"));
        Parent root = loader.load();

        mainScene = new Scene(root);
        AnimationsManager.applyGlobalCursor(mainScene);
        AnimationsManager.applyToAllButtons(root);

        stage.setScene(mainScene);
        stage.setFullScreen(true);
    }

    public static void switchTo(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(
                "/com/example/myfirstnavalbattle/scenes/"+ sceneName + "View/" + sceneName + ".fxml"));
        Parent newRoot = loader.load();

        AnimationsManager.applyGlobalCursor(mainScene);
        AnimationsManager.applyToAllButtons(newRoot);

        mainScene.setRoot(newRoot);
    }
}
