package com.example.myfirstnavalbattle.view;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnimationsManager {
    private static final ImageCursor cursorDefault;
    private static final ImageCursor cursorHover;
    private static final ImageCursor cursorPressed;
    private static final Map<Node, Animation[]> activeAnimations = new HashMap<>();

    static{
        cursorDefault = new ImageCursor(new Image(Objects.requireNonNull(AnimationsManager.class.getResourceAsStream("/com/example/myfirstnavalbattle/Images/CURSORDEFAULT.png"))));
        cursorHover = new ImageCursor(new Image(Objects.requireNonNull(AnimationsManager.class.getResourceAsStream("/com/example/myfirstnavalbattle/Images/CURSORHOVER.png"))), 28, 28);
        cursorPressed = new ImageCursor(new Image(Objects.requireNonNull(AnimationsManager.class.getResourceAsStream("/com/example/myfirstnavalbattle/Images/CURSORPRESSED.png"))),28, 28);
    }

    public static void applyGlobalCursor(Scene scene) {
        scene.setCursor(cursorDefault);
    }


    public static void applyToAllButtons(Parent root) {
        root.lookupAll(".button").forEach(node -> {
            if (node instanceof Button button) {
                applyAllEvents(button);
            }
        });
    }

    public static void applyAllEvents(Node node) {
        node.setOnMouseEntered(e -> translateAndScale(node, 0, 0, 1.1, 1.1, cursorHover));
        node.setOnMouseExited(e -> translateAndScale(node, 0, 0, 1.0, 1.0, cursorDefault));
        node.setOnMousePressed(e -> node.setCursor(cursorPressed));
        node.setOnMouseReleased(e -> node.setCursor(cursorHover));
    }

    public static void applyCursorEvents(Node node){
        node.setOnMouseEntered(e ->  node.setCursor(cursorHover));
        node.setOnMouseExited(e -> node.setCursor(cursorDefault));
        node.setOnMousePressed(e -> node.setCursor(cursorPressed));
        node.setOnMouseReleased(e -> node.setCursor(cursorHover));

    }

    public static void translateAndScale(Node node, double posX, double posY, double scaleX, double scaleY, ImageCursor cursor) {
        node.setCursor(cursor);
        //cancelAnimations(node);

        TranslateTransition move = new TranslateTransition(Duration.millis(200), node);
        move.setToX(posX);
        move.setToY(posY);

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setToX(scaleX);
        scale.setToY(scaleY);

        ParallelTransition animation = new ParallelTransition(move, scale);
        animation.play();

        activeAnimations.put(node, new Animation[]{animation});
    }




    private static void cancelAnimations(Node node) {
        Animation[] running = activeAnimations.get(node);
        if (running != null) {
            for (Animation anim : running) {
                if (anim.getStatus() == Animation.Status.RUNNING) {
                    anim.stop(); // Ahora es seguro, porque estás deteniendo solo la animación raíz
                }
            }
            activeAnimations.remove(node);
        }
    }

}
