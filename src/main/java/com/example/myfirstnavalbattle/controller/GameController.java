package com.example.myfirstnavalbattle.controller;


import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.model.Board;
import com.example.myfirstnavalbattle.model.ModelCell;
import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GameController {

    @FXML
    private GridPane gridPaneGame;
    private ModelCell[][] modelCells = null;
    private Ship[] shipsArray = null;

    private static Board board;

    public GameController() {
        gridPaneGame = null;
    }

    @FXML
    public void initialize() {

        gridPaneGame.setPrefSize(500, 500);
        gridPaneGame.setMaxSize(500, 500);
        gridPaneGame.setMinSize(500, 500);
        initGridGame();
        shipsArray = board.getModelShipsArray();
        initShipsImages();

        /*
        for (int x=0; x<cells.length; x++) {
            for (int y=0; y<cells[x].length; y++) {

                ModelCell mdc = modelCells[x][y];

                if(mdc.getStatus()== ModelCell.Status.SHIP){

                    System.out.println("SHIP IN: "+ mdc.getRow()+"-"+mdc.getCol());
                    System.out.println("SHIP SIZE: " + mdc.getShip().getSize());

                } else if (mdc.getStatus()==null) {

                    System.out.println("ERROR, MODEL CELLS SIN STATUS");
                }
            }
        }*/

    }

    private void initGridGame() {
        int size = SetupController.GRID_SIZE;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(50,50);
                stackPane.getStyleClass().add("StackPane");
                stackPaneListener(stackPane);

                GridPane.setRowIndex(stackPane, row);
                GridPane.setColumnIndex(stackPane, col);
                gridPaneGame.getChildren().add(stackPane);
            }
        }
    }

    private void stackPaneListener(StackPane stackPane) {
        stackPane.setOnMouseClicked(mouseEvent -> {
            System.out.println("Mouse Clicked");
        });
    }

    private void initShipsImages(){
        for (Ship ship : shipsArray) {

            int[] coords = (int[]) ship.getUserData();
            int row = coords[0];
            int col = coords[1];

            boolean vertical = ship.isVertical();
            int size = ship.getSize();
            Image image = ship.getImage();
            ImageView shipImage = new ImageView(image);
            shipImage.setMouseTransparent(true);

            putShipImage(row, col, shipImage, vertical, size);

        }
    }

    private void putShipImage(int row, int col, ImageView image, boolean vertical, int size) {
        int width = 48;
        int height = (50*size)-10;

        gridPaneGame.add(image, col, row);
        if (vertical) {
            image.setFitHeight(height);
            image.setFitWidth(width);
            GridPane.setColumnSpan(image, 1);
            GridPane.setRowSpan(image, size);
        }
        else{
            image.setFitHeight(width);
            image.setFitWidth(height);
            GridPane.setColumnSpan(image, size);
            GridPane.setRowSpan(image, 1);
        }
    }

    public static void setBoard(Board board) {
        GameController.board = board;
    }

    @FXML
    private void handleBackButton() throws IOException {
        SceneManager.switchScene("HomeScene");
    }
}
