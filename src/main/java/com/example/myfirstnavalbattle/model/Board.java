package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Board {

    Cell[][] setupCells = null;
    ModelCell[][] modelCellsArray = null;
    GridPane gridPane = null;

    Ship[] modelShips = null;
    int shipsCount = 0;

    public Board() {}

    public Board(GridPane grid, Cell[][] setupCells) {
        gridPane = grid;
        this.setupCells = setupCells;
        int rowsLength = setupCells.length;
        int columnsLength = setupCells[0].length;

        modelCellsArray = new ModelCell[rowsLength][columnsLength];
        modelShips = new Ship[Ship.shipsCount];

        initCells();
        initShips();
    }

    private void initCells() {
        for (int row = 0; row < setupCells.length; row++) {
            for (int col = 0; col < setupCells[row].length; col++) {

                ModelCell modelCell = new ModelCell(setupCells[row][col]);
                modelCellsArray[row][col] = modelCell;
            }
        }
    }

    private void initShips() {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Ship ship) {
                addShip(ship);
            }
        }
    }


    public void addShip(Ship ship) {
        modelShips[shipsCount] = ship;
        shipsCount++;
    }

    public Ship getShip(int row, int col) {
        return getCell(row, col).getShip();
    }

    public ModelCell getCell(int row, int col) {
        return modelCellsArray[row][col];
    }

    public ModelCell[][] getCellsArray() {
        return modelCellsArray;
    }

}
