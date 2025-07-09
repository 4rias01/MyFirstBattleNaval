package com.example.myfirstnavalbattle.controller;

import com.example.myfirstnavalbattle.controller.setupStage.Cell;
import com.example.myfirstnavalbattle.controller.setupStage.Ship;
import com.example.myfirstnavalbattle.model.Board;
import com.example.myfirstnavalbattle.model.ModelCell;
import com.example.myfirstnavalbattle.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GameController {

    @FXML
    private GridPane gameGrid;
    private ModelCell[][] modelCells = null;

    private static Board board;

    public GameController() {
        gameGrid = null;
    }

    @FXML
    public void initialize() {

        ModelCell[][] cells = board.getCellsArray();
        modelCells = cells;
        initGridGame();



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
        }

    }

    private void initGridGame() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(50,50);
                stackPane.getStyleClass().add("StackPane");

                GridPane.setRowIndex(stackPane, row);
                GridPane.setColumnIndex(stackPane, col);
                gameGrid.getChildren().add(stackPane);
            }
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
