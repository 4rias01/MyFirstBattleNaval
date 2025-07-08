package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import javafx.css.PseudoClass;

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



    public ModelCell() {
        this.status = ModelCell.Status.EMPTY;
        this.row = -1;
        this.col = -1;
    }

    public ModelCell(int row, int col, Status status) {
        this.status = status;
        this.row = row;
        this.col = col;
    }


    public void setStatus(Status status){
        this.status = status;
    }


    public Status getStatus() { return status; }
    public int getRow() { return row; }
    public int getCol() { return col; }
}
