package com.example.myfirstnavalbattle.controller;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.model.Board;
import com.example.myfirstnavalbattle.model.Characters;
import com.example.myfirstnavalbattle.model.SelectCharacter;
import com.example.myfirstnavalbattle.view.AnimationsManager;
import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class SetupController {
    private Cell[][] cells = null;
    private ArrayList<Ship> ships = null;
    private Characters actualCharacter;
    private final int CELL_SIZE = 50;
    public final static int GRID_SIZE = 10;

    private Ship currentShip;
    private boolean currentShipIsVertical;
    private int currentShipRow;
    private int currentShipCol;
    private int currentShipSize;

    @FXML private GridPane gridpane;
    @FXML private HBox hBox;
    @FXML private Button readyButton;
    @FXML private ImageView characterImage;
    @FXML private TextField userNameTextField;


    @FXML
    public void initialize() {
        ships = new ArrayList<>();
        cells = new Cell[GRID_SIZE][GRID_SIZE];

        initGridPane();
        initShips();
        initUserInfo();

        gridpane.setPrefSize(500, 500);
        gridpane.setMaxSize(500, 500);
        gridpane.setMinSize(500, 500);

    }

    private void initUserInfo(){
        actualCharacter = SelectCharacter.getSelectedCharacter();
        Image image = actualCharacter.getImage();

        characterImage.setImage(image);
        characterImage.setFitHeight(300);
        characterImage.setFitWidth(300);
        characterImage.setVisible(false);

        readyButton.setDisable(true);
        readyButton.setVisible(false);

        userNameTextField.setDisable(true);
        userNameTextField.setVisible(false);
        userNameTextField.textProperty().addListener(
                (obs, oldText, newText)
                        -> readyButton.setDisable(newText.length() <= 3));
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

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Cell cell = new Cell(row, col);
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);

                cells[row][col] = cell;

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


        int targetRow = cell.getRow();
        int targetCol = cell.getCol();

        boolean canBePlace = canBePlaced(targetRow, targetCol);

        if (canBePlace && currentShip.getParent() instanceof GridPane) {
            setCellState(Cell.Status.EMPTY, null);
            currentShip.setUserData(new int[]{targetRow, targetCol});
            setCurrentShipLocation();

            setCellState(Cell.Status.SHIP, currentShip);
            placeShipInGridPane();
        } else if (canBePlace) {
            currentShip.setUserData(new int[]{targetRow, targetCol});
            setCurrentShipLocation();

            setCellState(Cell.Status.SHIP, currentShip);
            placeShipInGridPane();
        }

    }


    private void setCellState(Cell.Status status, Ship ship) {
        int init = currentShipIsVertical? currentShipRow : currentShipCol; // variable que ira iterando el for.
        // Si es vertical, itera el row y col permanece fijo
        // si es horizontal, el row permanece fijo y itera el col

        for (int target = init; target < init + currentShipSize; target++) {

            Cell cell;
            if (currentShipIsVertical) {
                cell = getCell(target, currentShipCol); //iteras el row
            }
            else{
                cell = getCell(currentShipRow, target); //iteras el col
            }
            assert cell != null;
            cell.setStatus(status);
            cell.setShip(ship);
        }
    }

    private void placeShipInGridPane(){
        if (currentShip.getParent() != null){
            ((Pane) currentShip.getParent()).getChildren().remove(currentShip);
        }

        if (currentShipIsVertical){
            gridpane.add(currentShip, currentShipCol, currentShipRow);
            GridPane.setRowSpan(currentShip, currentShipSize);
            GridPane.setColumnSpan(currentShip, 1);
        }
        else{
            gridpane.add(currentShip, currentShipCol, currentShipRow);
            GridPane.setRowSpan(currentShip, 1);
            GridPane.setColumnSpan(currentShip, currentShipSize);
        }
    }

    private boolean canBePlaced(int row, int col) {
        int init = currentShipIsVertical ? row : col;
        for (int target = init; target < init + currentShipSize; target++) {

            Cell cell;
            if (currentShipIsVertical) {
                cell = getCell(target,col);
            }
            else {
                cell = getCell(row,target);
            }

            if (cell == null) { return false; }
            if (cell.getStatus() == Cell.Status.SHIP) { return false; }
        }
        return true;
    }


    private void makeAndAddShip(int size) {
        Ship ship = new Ship(size);
        AnimationsManager.applyCursorEvents(ship)   ;

        shipOnDragListener(ship);
        clickOnShipListener(ship);
        hBox.getChildren().add(ship);
        ships.add(ship);
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
            currentShip = ship;
            setCurrenShipAttributes();

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
                currentShip = ship;
                setCurrenShipAttributes();
                setCurrentShipLocation();

                setCellState(Cell.Status.EMPTY, null);
                ((Pane) ship.getParent()).getChildren().remove(ship);

                if (!currentShipIsVertical) {
                    ship.rotateShip();
                }
                hBox.getChildren().add(ship);
                activateUserInfo();
            }
        });

    }


    private void cellOnDragOver(Cell cell) {
        cell.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            if (currentShip != null) {
                currentShip.setUserData(new int[] {cell.getRow(), cell.getCol()});
                setCurrenShipAttributes();
                setCurrentShipLocation();
                if (canBePlaced(cell.getRow(), cell.getCol())) {
                    setCellState(Cell.Status.OVER, null);
                }
            }
            event.consume();
        });
        cell.setOnDragExited(event -> {
            setCurrenShipAttributes();
            if (currentShip != null && cell.getStatus() == Cell.Status.OVER) {
                setCellState(Cell.Status.EMPTY, null);
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
            activateUserInfo();
        });
    }

    public Image getShipSnapshot(Ship ship) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return ship.snapshot(params, null);
    }


    private Cell getCell(int row, int col) {
        if (row >= GRID_SIZE || col >= GRID_SIZE) {
            return null;
        }
        return cells[row][col];
    }


    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchScene("HomeScene");
    }

    @FXML
    private void handleReadyButton() throws IOException {
        Board board = new Board(cells, ships);
        GameController.setBoard(board);
        actualCharacter.setUsername(userNameTextField.getText());
        SceneManager.switchScene("GameScene");
    }


    private void activateUserInfo() {
        if(hBox.getChildren().isEmpty()){
            readyButton.setVisible(true);
            characterImage.setVisible(true);
            userNameTextField.setVisible(true);
            userNameTextField.setDisable(false);
        }
        else{
            characterImage.setVisible(false);
            readyButton.setVisible(false);
            readyButton.setDisable(true);
            userNameTextField.setDisable(true);
            userNameTextField.setVisible(false);
            userNameTextField.setText("");
        }
    }

    private void setCurrenShipAttributes(){
        currentShipSize = currentShip.getSize();
        currentShipIsVertical = currentShip.isVertical();
    }

    private void setCurrentShipLocation(){
        int[] coords = (int[]) currentShip.getUserData();
        currentShipRow = coords[0];
        currentShipCol = coords[1];
    }

}
