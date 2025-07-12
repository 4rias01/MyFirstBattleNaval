package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;

import java.util.ArrayList;

public class Player {
    private final Board board;
    private final String playerName;
    private final Characters character;
    private boolean hasPlayed;
    private boolean hasLost;

    public Player (String playerName, Cell[][] setupCells, ArrayList<Ship> setupShips, Characters character) {
        this.board = new Board(setupCells, setupShips);
        this.playerName = playerName;
        this.character = character;
        hasPlayed = false;
        hasLost = false;
    }

    public Player () {
        board = new Board();
        character = new Characters("DEFAULT");
        playerName = "IA";
        hasPlayed = false;
        hasLost = false;
    }

    public ModelCell.Status shoot(int row, int col) {
        ModelCell.Status status = board.shoot(row, col);
        if (status == ModelCell.Status.MISS){
            hasPlayed = true;
        }
        else if (status == ModelCell.Status.KILLED){
            if (!board.stillShipsAlive()){
                hasLost = true;
            }
        }
        return status;
    }

    public Board getBoard() { return board; }
    public String getPlayerName() { return playerName; }
    public Characters getCharacter() { return character; }
    public boolean isHasPlayed() { return hasPlayed; }
    public boolean isHasLost() { return hasLost; }

    public void setHasPlayed(boolean hasPlayed) { this.hasPlayed = hasPlayed; }
}
