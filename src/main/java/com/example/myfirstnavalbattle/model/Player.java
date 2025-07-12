package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;

import java.util.ArrayList;

public class Player {
    private final Board board;
    private final String playerName;
    private final Characters character;
    private boolean hasPlayed;
    private boolean hasWon;

    public Player (String playerName, Cell[][] setupCells, ArrayList<Ship> setupShips, Characters character) {
        this.board = new Board(setupCells, setupShips);
        this.playerName = playerName;
        this.character = character;
        hasPlayed = false;
        hasWon = false;
    }

    public Player () {
        board = new Board();
        character = new Characters("DEFAULT");
        playerName = "IA";
        hasPlayed = true;
        hasWon = false;
    }

    public Board getBoard() { return board; }
    public String getPlayerName() { return playerName; }
    public Characters getCharacter() { return character; }
    public boolean hasPlayed() { return hasPlayed; }
    public boolean hasWon() { return hasWon; }

    public void setHasPlayed(boolean hasPlayed) { this.hasPlayed = hasPlayed; }
    public void setHasWon(boolean hasWon) { this.hasWon = hasWon; }
}
