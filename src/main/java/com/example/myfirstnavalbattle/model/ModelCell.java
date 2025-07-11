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
    private Ship ship;


    public ModelCell(Cell cell) {
        row = cell.getRow();
        col = cell.getCol();
        this.ship = cell.getShip();

        this.status = switch (cell.getStatus()) {
            case EMPTY -> Status.EMPTY;
            case SHIP -> Status.SHIP;
            default -> null;
        };
    }

    public ModelCell(int row, int col) {
        this.row = row;
        this.col = col;
        ship = null;
        status = Status.EMPTY;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Ship getShip() {
        return ship;
    }
    public void setShip(Ship ship) { this.ship = ship; }

    public Status getStatus() { return status; }
    public int getRow() { return row; }
    public int getCol() { return col; }
}
