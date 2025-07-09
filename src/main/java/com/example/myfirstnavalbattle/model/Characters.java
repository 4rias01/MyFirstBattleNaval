package com.example.myfirstnavalbattle.model;

import javafx.scene.image.Image;

import java.util.Objects;

public class Characters {

    private final String name;
    private final Image image;
    private String username;


    public Characters(String name) {
        this.name = "Capitán "+ name;
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/myfirstnavalbattle/Images/captains/CAPTAIN" + name.toUpperCase() + ".png")));
        this.username = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public String getUsername() { return username; }
    public Image getImage() {return image;}

}
