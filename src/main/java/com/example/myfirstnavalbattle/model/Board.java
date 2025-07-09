package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.SetupController;
import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Board {

    ModelCell[][] modelCellsArray = null;
    ArrayList<Ship> modelShipsArray = null;
    GridPane gridPane = null;

    int shipsCount = 0;

    public Board() {}

    public Board(GridPane grid, Cell[][] setupCellsArray, ArrayList<Ship> shipsArray) {
        gridPane = grid;
        int size = SetupController.GRID_SIZE;

        modelShipsArray = new ArrayList<>();
        modelCellsArray = new ModelCell[size][size];

        initCells(setupCellsArray);
        initShips(shipsArray);
    }

    private void initCells(Cell[][] setupCellsArray) {
        for (int row = 0; row < setupCellsArray.length; row++) {
            for (int col = 0; col < setupCellsArray[row].length; col++) {

                ModelCell modelCell = new ModelCell(setupCellsArray[row][col]);
                modelCellsArray[row][col] = modelCell;
            }
        }
    }

    private void initShips(ArrayList<Ship> shipsArray) {
        modelShipsArray.addAll(shipsArray);
    }


    public Ship getShip(int row, int col) {
        return getCell(row, col).getShip();
    }

    public ArrayList<Ship> getModelShipsArray() {
        return modelShipsArray;
    }

    public ModelCell getCell(int row, int col) {
        return modelCellsArray[row][col];
    }

    public ModelCell[][] getCellsArray() {
        return modelCellsArray;
    }

}
