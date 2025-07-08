package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;

public class Board {
    ModelCell[][] modelCells = new ModelCell[10][10];

    public Board() {}

    public Board(Cell cells[][]) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                ModelCell modelCell = cellToModel(cells[row][col]);
                modelCells[row][col] = modelCell;
            }
        }
    }

    private ModelCell cellToModel(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        ModelCell.Status status = switch (cell.getStatus()) {
            case EMPTY -> ModelCell.Status.EMPTY;
            case SHIP -> ModelCell.Status.SHIP;
            default -> null;
        };

        ModelCell modelCell = new ModelCell(row, col);
        modelCell.setStatus(status);
        return modelCell;
    }

}
