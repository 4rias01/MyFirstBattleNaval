package com.example.myfirstnavalbattle.model;

import com.example.myfirstnavalbattle.controller.SetupController;
import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Board {

    ModelCell[][] playerCells;
    ArrayList<Ship> playerShips;

    ModelCell[][] iaCells;
    ArrayList<Ship> iaShips;
    GridPane gridPane;
    int size;


    public Board(GridPane grid, Cell[][] setupCells, ArrayList<Ship> setupShips) {
        gridPane = grid;
        size = SetupController.GRID_SIZE;

        playerCells = new ModelCell[size][size];
        playerShips = new ArrayList<>();

        iaCells = new ModelCell[size][size];
        iaShips = new ArrayList<>();

        initPlayerBoard(setupCells, setupShips);
        initIABoard(setupShips, size);
    }

    private void initPlayerBoard(Cell[][] setupCellsArray, ArrayList<Ship> setupShips) {
        for (int row = 0; row < setupCellsArray.length; row++) {
            for (int col = 0; col < setupCellsArray[row].length; col++) {

                ModelCell modelCell = new ModelCell(setupCellsArray[row][col]);
                playerCells[row][col] = modelCell;
            }
        }
        initPlayerShips(setupShips);
    }

    private void initIABoard(ArrayList<Ship> ships, int sizeOfGrid) {
        for (Ship ship : ships) {
            Ship iaShip = new Ship(ship.getSize());
            iaShips.add(iaShip);
        }

        for (int row = 0; row < sizeOfGrid; row++) {
            for (int col = 0; col < sizeOfGrid; col++) {
                ModelCell iaCell = new ModelCell(row, col);
                iaCells[row][col] = iaCell;
            }
        }

        generateIABoard();
    }

    private void generateIABoard() {
        for (Ship ship : iaShips) {
            int size = ship.getSize();

            while (true) {
                boolean vertical = Math.random() < 0.5;
                int row = (int)(Math.random() * 10);
                int col = (int)(Math.random() * 10);
                if (canBePlaceShipInIA(row, col, vertical, size)) {

                    if (ship.isVertical() != vertical) {
                        ship.rotateShip();
                    }

                    ship.setUserData( new int[] {row, col});
                    setModelCellsState(ship, row, col, size, vertical);
                    break;
                }
            }
        }
    }

    private void setModelCellsState(Ship ship, int row, int col, int size, boolean vertical) {
        int init = vertical? row : col; // variable que ira iterando el for.
        // Si es vertical, itera el row y col permanece fijo
        // si es horizontal, el row permanece fijo y itera el col

        for (int target = init; target < init + size; target++) {

            ModelCell cell;
            if (vertical) {
                cell = getIACell(target, col); //iteras el row
            }
            else{
                cell = getIACell(row, target); //iteras el col
            }
            assert cell != null;
            cell.setStatus(ModelCell.Status.SHIP);
            cell.setShip(ship);
        }
    }

    private boolean canBePlaceShipInIA(int row, int col, boolean vertical, int size) {
        int init = vertical ? row : col;
        for (int target = init; target < init + size; target++) {

            ModelCell cell;
            if (vertical) {
                cell = getIACell(target,col);
            }
            else {
                cell = getIACell(row,target);
            }

            if (cell == null) { return false; }
            if (cell.getStatus() == ModelCell.Status.SHIP) { return false; }
        }
        return true;
    }

    private void initPlayerShips(ArrayList<Ship> setupShips) {
        playerShips.addAll(setupShips);
    }


    public Ship getShip(int row, int col) {
        return getPlayerCell(row, col).getShip();
    }

    public ArrayList<Ship> getPlayerShips() {
        return playerShips;
    }

    public ModelCell getPlayerCell(int row, int col) {
        return playerCells[row][col];
    }

    public ModelCell getIACell(int row, int col) {
        if (row >= size || col >= size) {
            return null;
        }
        return iaCells[row][col];
    }

    public ArrayList<Ship> getIAShips() {
        return iaShips;
    }

}
