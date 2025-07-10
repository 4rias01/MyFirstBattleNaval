package com.example.myfirstnavalbattle.controller;


import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.model.Board;
import com.example.myfirstnavalbattle.model.ModelCell;
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

public class GameController {

    @FXML
    private GridPane gridPanePlayer;
    private ArrayList<Ship> playerShips = null;

    @FXML
    private GridPane gridPaneIA;
    private ArrayList<Ship> iaShips = null;
    private ArrayList<ImageView> iaShipsImageView = null;

    private StackPane[][] iaBoardStackPanes = null;

    @FXML
    AnchorPane anchorPane;

    private static Board board;

    public GameController() {
        gridPanePlayer = null;
    }

    @FXML
    public void initialize() {

        gridPanePlayer.setPrefSize(500, 500);
        gridPanePlayer.setMaxSize(500, 500);
        gridPanePlayer.setMinSize(500, 500);

        gridPaneIA.setPrefSize(500, 500);
        gridPaneIA.setMaxSize(500, 500);
        gridPaneIA.setMinSize(500, 500);

        addListenerToScene(anchorPane);
        int size = SetupController.GRID_SIZE;
        iaBoardStackPanes = new StackPane[size][size];

        playerShips = board.getPlayerShips();
        iaShips = board.getIAShips();
        iaShipsImageView = new ArrayList<>();
        initGridPanes();
        initShips();

    }

    private void initGridPanes() {
        int size = SetupController.GRID_SIZE;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                StackPane stackPanePlayer = new StackPane();
                stackPanePlayer.setPrefSize(50,50);
                stackPanePlayer.getStyleClass().add("StackPane");

                StackPane stackPaneIA = new StackPane();
                stackPaneIA.setPrefSize(50,50);
                stackPaneIA.getStyleClass().add("StackPane");
                stackPaneListener(stackPaneIA);
                stackPaneIA.setUserData(new int[]{row, col});

                GridPane.setRowIndex(stackPanePlayer, row);
                GridPane.setColumnIndex(stackPanePlayer, col);
                gridPanePlayer.getChildren().add(stackPanePlayer);

                GridPane.setRowIndex(stackPaneIA, row);
                GridPane.setColumnIndex(stackPaneIA, col);
                gridPaneIA.getChildren().add(stackPaneIA);
                iaBoardStackPanes[row][col] = stackPaneIA;
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

        int width = 48;
        int height = (50*size)-10;

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
    }

    private void stackPaneListener(StackPane stackPane) {
        stackPane.setOnMouseClicked(mouseEvent -> {

            System.out.println("Mouse Clicked");
            stackPane.setDisable(true);

            int[] coords = (int[]) stackPane.getUserData();
            ModelCell.Status status= board.shoot(coords[0], coords[1]);

            if (status == ModelCell.Status.MISS) {
                stackPane.getStyleClass().add("water");
            }
            else if (status == ModelCell.Status.HIT) {
                stackPane.getStyleClass().add("hit");
            }
            else if (status == ModelCell.Status.KILLED) {
                Ship targetShip = board.getShip(coords[0], coords[1]);
                int[] targetCoords = (int[]) targetShip.getUserData();
                int row = targetCoords[0];
                int col = targetCoords[1];

                setStackPaneState(targetShip, row, col, targetShip.getSize(), targetShip.isVertical());
            }
        });
    }

    private void setStackPaneState(Ship ship, int row, int col, int size, boolean vertical) {
        int init = vertical? row : col;

        for (int target = init; target < init + size; target++) {

            StackPane stackPane;
            if (vertical) {
                stackPane = getIAStacPane(target, col); //iteras el row
            }
            else{
                stackPane = getIAStacPane(row, target); //iteras el col
            }
            assert stackPane != null;
            stackPane.getStyleClass().add("killed");
        }
    }

    private StackPane getIAStacPane(int row, int col) {
        return iaBoardStackPanes[row][col];
    }


    public static void setBoard(Board board) {
        GameController.board = board;
    }

    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchScene("HomeScene");
    }

    private void setIAView(boolean show){
        for (ImageView imageView : iaShipsImageView) {
            imageView.setVisible(show);
        }
    }

    private void addListenerToScene(AnchorPane pane) {
        pane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.isControlDown() && event.getCode() == KeyCode.S) {
                        setIAView(!iaShipsImageView.get(0).isVisible());
                        event.consume();
                    }
                });
            }
        });
    }
}
