package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.SetupController;
import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Board {

    Cell[][] setupCellsArray = null;
    ModelCell[][] modelCellsArray = null;
    Ship[] modelShipsArray = null;
    GridPane gridPane = null;

    int shipsCount = 0;

    public Board() {}

    public Board(GridPane grid, Cell[][] setupCellsArray) {
        gridPane = grid;
        this.setupCellsArray = setupCellsArray;
        int size = SetupController.GRID_SIZE;

        modelCellsArray = new ModelCell[size][size];
        modelShipsArray = new Ship[Ship.shipsCount];

        initCells();
        initShips();
    }

    private void initCells() {
        for (int row = 0; row < setupCellsArray.length; row++) {
            for (int col = 0; col < setupCellsArray[row].length; col++) {

                ModelCell modelCell = new ModelCell(setupCellsArray[row][col]);
                modelCellsArray[row][col] = modelCell;
            }
        }
    }

    private void initShips() {
        int pos = 0;
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Ship ship) {
                modelShipsArray[pos] = ship;
                int[]coords = (int[]) ship.getUserData();
                pos++;
            }
        }
    }


    public Ship getShip(int row, int col) {
        return getCell(row, col).getShip();
    }

    public Ship[] getModelShipsArray() {
        return modelShipsArray;
    }

    public ModelCell getCell(int row, int col) {
        return modelCellsArray[row][col];
    }

    public ModelCell[][] getCellsArray() {
        return modelCellsArray;
    }

}
