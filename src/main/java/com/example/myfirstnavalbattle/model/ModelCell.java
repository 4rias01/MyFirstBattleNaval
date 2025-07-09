package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;

public class ModelCell {


    public enum Status {
        EMPTY,
        SHIP,
        MISS,
        HIT,
        KILLED
    }

    private Status status;

    private final int row;
    private final int col;
    private final Ship ship;


    public ModelCell(Cell cell) {
        row = cell.getRow();
        col = cell.getCol();
        this.ship = cell.getShip();

        ModelCell.Status status = switch (cell.getStatus()) {
            case EMPTY -> ModelCell.Status.EMPTY;
            case SHIP -> ModelCell.Status.SHIP;
            default -> null;
        };
        this.status = status;
    }


    public void setStatus(Status status){
        this.status = status;
    }

    public Ship getShip() {
        return ship;
    }

    public Status getStatus() { return status; }
    public int getRow() { return row; }
    public int getCol() { return col; }
}
