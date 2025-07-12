package com.example.myfirstnavalbattle.controller;


import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.model.Board;
import com.example.myfirstnavalbattle.model.ModelCell;
import com.example.myfirstnavalbattle.model.Player;
import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    @FXML
    private GridPane gridPanePlayer;
    private ArrayList<Ship> playerShips = null;

    @FXML
    private GridPane gridPaneIA;
    private ArrayList<Ship> iaShips = null;
    private ArrayList<ImageView> iaShipsImageView = null;

    private StackPane[][] stackPanesOfIA = null;

    @FXML
    AnchorPane anchorPane;

    private static Player playerOne;
    private static Player playerIA;
    private Board playerOneBoard;
    private Board playerIABoard;

    public GameController() {
    }

    @FXML
    public void initialize() {

        int size = SetupController.GRID_SIZE;
        int margins = 450;
        addListenerToScene(anchorPane);

        stackPanesOfIA = new StackPane[size][size];

        playerOneBoard = playerOne.getBoard();
        playerIABoard = playerIA.getBoard();
        assert playerOneBoard != null;

        playerShips = playerOneBoard.getShips();
        iaShips = playerIABoard.getShips();
        assert iaShips != null;
        iaShipsImageView = new ArrayList<>();

        initGridPane(gridPanePlayer, margins, size, 45);
        initGridPane(gridPaneIA, margins, size, 45);

        initIAStackPaneListener(size);

        initShips();
    }

    private void initGridPane(GridPane gridPane, int margins, int size, int stackSize) {
        gridPane.setPrefSize(margins, margins);
        gridPane.setMaxSize(margins, margins);
        gridPane.setMinSize(margins, margins);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(stackSize,stackSize);
                stackPane.getStyleClass().add("StackPane");

                GridPane.setRowIndex(stackPane, row);
                GridPane.setColumnIndex(stackPane, col);
                gridPane.getChildren().add(stackPane);

                if (gridPane == gridPaneIA) {
                    stackPanesOfIA[row][col] = stackPane;
                }
            }
        }
    }

    private void initIAStackPaneListener(int size){
        StackPane stackPane;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                stackPane = stackPanesOfIA[row][col];
                stackPaneListener(stackPane);
                stackPane.setUserData(new int[]{row, col});
            }
        }
    }


    private void initShips(){
        for (int index = 0; index < iaShips.size(); index++) {
            Ship playerShip = playerShips.get(index);
            Ship iaShip = iaShips.get(index);

            ImageView playerShipImage = new ImageView(playerShip.getImage());
            ImageView iaShipImage = new ImageView(iaShip.getImage());
            iaShipsImageView.add(iaShipImage);

            putShipImage(playerShip, gridPanePlayer, playerShipImage);
            putShipImage(iaShip, gridPaneIA, iaShipImage);

        }
        setIAView(false);
    }

    private void putShipImage(Ship ship, GridPane gridPane, ImageView shipImage) {
        shipImage.setMouseTransparent(true);
        int[] coords = (int[]) ship.getUserData();
        int row = coords[0];
        int col = coords[1];

        boolean vertical = ship.isVertical();
        int size = ship.getSize();

        int width = 43;
        int height = (45*size)-10;

        gridPane.add(shipImage, col, row);
        if (vertical) {
            shipImage.setFitHeight(height);
            shipImage.setFitWidth(width);
            GridPane.setColumnSpan(shipImage, 1);
            GridPane.setRowSpan(shipImage, size);
        }
        else{
            shipImage.setFitHeight(width);
            shipImage.setFitWidth(height);
            GridPane.setColumnSpan(shipImage, size);
            GridPane.setRowSpan(shipImage, 1);
        }
        shipImage.setUserData(coords);
    }

    private void stackPaneListener(StackPane stackPane) {
        stackPane.setOnMouseClicked(mouseEvent -> {

            stackPane.setDisable(true);

            int[] coords = (int[]) stackPane.getUserData();
            ModelCell.Status status = playerIABoard.shoot(coords[0], coords[1]);

            if (status == ModelCell.Status.MISS) {
                stackPane.getStyleClass().add("water");
            }
            else if (status == ModelCell.Status.HIT) {
                stackPane.getStyleClass().add("hit");
            }
            else if (status == ModelCell.Status.KILLED) {
                Ship targetShip = playerIABoard.getShip(coords[0], coords[1]);
                int[] targetCoords = (int[]) targetShip.getUserData();
                int row = targetCoords[0];
                int col = targetCoords[1];

                setImageVisibility(row, col);
                setStackPaneState(row, col, targetShip.getSize(), targetShip.isVertical());
            }
        });
    }

    private void setStackPaneState(int row, int col, int size, boolean vertical) {
        int init = vertical? row : col;

        for (int target = init; target < init + size; target++) {

            StackPane stackPane;
            if (vertical) {
                stackPane = getIAStackPane(target, col); //iteras el row
            }
            else{
                stackPane = getIAStackPane(row, target); //iteras el col
            }
            assert stackPane != null;
            stackPane.getStyleClass().add("killed");
        }
    }

    private StackPane getIAStackPane(int row, int col) {
        return stackPanesOfIA[row][col];
    }


    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchTo("HomeScene");
    }

    private void setIAView(boolean show){
        for (ImageView imageView : iaShipsImageView) {
            imageView.setVisible(show);
        }
    }

    private void setImageVisibility(int row, int col) {
        int[] coords = new int[]{row, col};
        for (ImageView imageView : iaShipsImageView) {

            int[] imageCoords = (int[]) imageView.getUserData();
            if (Arrays.equals(coords, imageCoords)) {
                imageView.setVisible(true);
                iaShipsImageView.remove(imageView);
                break;
            }
        }
    }

    private void addListenerToScene(AnchorPane pane) {
        pane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.isControlDown() && event.getCode() == KeyCode.S && !iaShipsImageView.isEmpty()) {
                        setIAView(!iaShipsImageView.get(0).isVisible());
                        event.consume();
                    }
                });
            }
        });
    }

    public static void setPlayerOne(Player player) {
        playerOne = player;
    }

    public static void setPlayerIA(Player player) {
        playerIA = player;
    }
}
