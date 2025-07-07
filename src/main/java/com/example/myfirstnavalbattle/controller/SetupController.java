package com.example.myfirstnavalbattle.controller;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.view.AnimationsManager;
import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class SetupController {
    @FXML
    private GridPane gridpane;
    @FXML
    private HBox hBox;
    @FXML
    private Button readyButton;


    private final int CELL_SIZE = 50;


    @FXML
    public void initialize() {
        readyButton.setDisable(true);
        initGridPane();
        initShips();
        gridpane.setPrefSize(500, 500);
        gridpane.setMaxSize(500, 500);
        gridpane.setMinSize(500, 500);
    }

    private void initShips() {
        makeAndAddShip(4);
        makeAndAddShip(3);
        makeAndAddShip(3);
        makeAndAddShip(2);
        makeAndAddShip(2);
        makeAndAddShip(2);
        makeAndAddShip(1);
        makeAndAddShip(1);
        makeAndAddShip(1);
        makeAndAddShip(1);
    }

    @FXML
    private void initGridPane() {
        int gridPaneSize = 10;
        for (int row = 0; row < gridPaneSize; row++) {
            for (int col = 0; col < gridPaneSize; col++) {
                Cell cell = new Cell(row, col);
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);


                cellOnDragOver(cell);
                cellOnDragDropped(cell);

                GridPane.setRowIndex(cell, row);
                GridPane.setColumnIndex(cell, col);
                gridpane.getChildren().add(cell);
            }
        }
    }


    @FXML
    private void dropShipInCell(Cell cell) {

        Ship ship = Ship.currentlyDraggedShip;
        int size = ship.getSize();
        boolean vertical = ship.isVertical();

        int targetRow = cell.getRow();
        int targetCol = cell.getCol();

        int spanCols = (int) Math.ceil(ship.getWidth() / CELL_SIZE);  // = 2
        int spanRows = (int) Math.ceil(ship.getHeight() / CELL_SIZE);

        boolean canBePlace = canBePlaced(targetRow, targetCol, ship);

        if (canBePlace && ship.getParent() instanceof GridPane) {
            int oldRow, oldCol;
            int[] coords = (int[]) ship.getUserData();
            oldRow = coords[0];
            oldCol = coords[1];
            setCellState(oldRow, oldCol, size, vertical, Cell.Status.EMPTY);

            ship.setUserData(new int[]{targetRow, targetCol});
            setCellState(targetRow, targetCol, size, vertical, Cell.Status.SHIP);
            placeShipInGridPane(ship, targetCol, targetRow, spanCols, spanRows);
        } else if (canBePlace) {
            ship.setUserData(new int[]{targetRow, targetCol});
            setCellState(targetRow, targetCol, size, vertical, Cell.Status.SHIP);
            placeShipInGridPane(ship, targetCol, targetRow, spanCols, spanRows);
        }

    }


    private void setCellState(int row, int col, int size, boolean vertical, Cell.Status status){
        if (vertical) {
            for (int targetRow = row; targetRow < row + size; targetRow++) {
                Cell cell = getCell(targetRow,col);
                assert cell != null;
                cell.setStatus(status);
            }
        } else {
            for (int targetCol = col; targetCol < col + size; targetCol++) {
                Cell cell = getCell(row, targetCol);
                assert cell != null;
                cell.setStatus(status);
            }
        }
    }

    @FXML
    private void placeShipInGridPane(Ship ship, int col, int row, int spanCols, int spanRows) {
        if (ship.getParent() != null) {
            ((Pane) ship.getParent()).getChildren().remove(ship);
        }
        gridpane.add(ship, col, row);
        GridPane.setColumnSpan(ship, spanCols);
        GridPane.setRowSpan(ship, spanRows);
    }




    private boolean canBePlaced(int row, int col, Ship ship) {
        int spanCols = (int) Math.ceil(ship.getWidth() / CELL_SIZE);  // = 4
        int spanRows = (int) Math.ceil(ship.getHeight() / CELL_SIZE);

        for (int targetRow = row; targetRow < row + spanRows; targetRow++) {
            for (int targetCol = col; targetCol < col + spanCols; targetCol++) {

                Cell cell = getCell(targetRow, targetCol);
                if (cell == null) { return false; }
                if (cell.getStatus() == Cell.Status.SHIP) { return false; }
            }
        }

        return true;
    }


    private void makeAndAddShip(int size) {
        Ship ship = new Ship(size);
        AnimationsManager.applyCursorEvents(ship)   ;

        shipOnDragListener(ship);
        clickOnShipListener(ship);
        hBox.getChildren().add(ship);
    }

    private void shipOnDragListener(Ship ship) {
        ship.setOnDragDetected(event -> {
            Dragboard db = ship.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("ship");

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image snapshot = getShipSnapshot(ship);
            content.putImage(snapshot);

            if (ship.isVertical()) { db.setDragView(snapshot, snapshot.getWidth() / 2, 25 );}
            else { db.setDragView(snapshot, 25, snapshot.getHeight() / 2 );}

            db.setContent(content);
            Ship.currentlyDraggedShip = ship;

            event.consume();
        });
    }


    private void clickOnShipListener(Ship ship) {
        ship.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY){
                if (!(ship.getParent() instanceof GridPane)) {
                    ship.rotateShip();
                }
            }
            if (event.getClickCount() == 2 && ship.getParent() instanceof GridPane) {

                int[] coords = (int[]) ship.getUserData();
                boolean vertical = ship.isVertical();
                int size = ship.getSize();
                int row, col;

                row = coords[0];
                col = coords[1];

                setCellState(row, col, size, vertical, Cell.Status.EMPTY);

                ((Pane) ship.getParent()).getChildren().remove(ship);
                if (!vertical) {
                    ship.rotateShip();
                }
                hBox.getChildren().add(ship);
                activateButton();
            }
        });

    }


    private void cellOnDragOver(Cell cell) {
        cell.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            if (Ship.currentlyDraggedShip != null) {
                Ship ship = Ship.currentlyDraggedShip;
                if (canBePlaced(cell.getRow(), cell.getCol(), ship)) {
                    setCellState(cell.getRow(), cell.getCol(), ship.getSize(), ship.isVertical(), Cell.Status.OVER);
                }
            }
            event.consume();
        });
        cell.setOnDragExited(event -> {
            if (Ship.currentlyDraggedShip != null && cell.getStatus() == Cell.Status.OVER) {
                Ship ship = Ship.currentlyDraggedShip;
                setCellState(cell.getRow(), cell.getCol(), ship.getSize(), ship.isVertical(), Cell.Status.EMPTY);

            }
        });
    }

    private void cellOnDragDropped(Cell cell) {
        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                dropShipInCell(cell);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
            activateButton();
        });
    }

    public Image getShipSnapshot(Ship ship) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return ship.snapshot(params, null);
    }

    @FXML
    private Cell getCell(int row, int col) {
        for (Node node : gridpane.getChildren()) {
            if (safeRowIndex(node) == row && safeColIndex(node) == col && node instanceof Cell) {
                return (Cell) node;
            }
        }
        return null;
    }

    private int safeRowIndex(Node node) {
        Integer value = GridPane.getRowIndex(node);
        return value != null ? value : 0;
    }

    private int safeColIndex(Node node) {
        Integer value = GridPane.getColumnIndex(node);
        return value != null ? value : 0;
    }

    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchScene("HomeScene");
    }

    @FXML
    private void handleReadyButton() throws IOException {

    }

    private void activateButton() {
        readyButton.setDisable(!hBox.getChildren().isEmpty());
    }

}
