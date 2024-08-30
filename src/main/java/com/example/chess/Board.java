package com.example.chess;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;


import java.util.Objects;

public class Board {

    public final static int BOARD_SIZE = 8;
    public final static int SQUARE_SIZE = 80;
    public Square[] highlightedSquares = new Square[2];

    public void initializeBoard(Group root) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int id = 8*col+row;
                Square rectangle = new Square(id);
                rectangle.setX(row * SQUARE_SIZE);
                rectangle.setY(col * SQUARE_SIZE);
                addEvents(rectangle);
                rectangle.setWidth(SQUARE_SIZE);
                rectangle.setHeight(SQUARE_SIZE);
                rectangle.setId(id+"r");
                rectangle.setFill(rectangle.precomputedColors[0]);

                root.getChildren().add(rectangle);
            }
        }
    }

    public void addEvents(Square rectangle){
        rectangle.setOnMouseReleased(event ->{
            if(event.getButton() == MouseButton.SECONDARY){
                getNextColor(rectangle);
            }

        });

    }

    public void unHighlight(ObservableList<Node> nodes){
        for(Node node : nodes){
            if(node instanceof Square square){
                square.setFill(square.getCurrentColor());
            }
        }
    }
    public static void getNextColor(Square rectangle){
        if(Objects.equals(rectangle.getFill(),rectangle.precomputedColors[1])){
            rectangle.setFill(rectangle.getCurrentColor());
        }
        else{
            rectangle.setFill(rectangle.precomputedColors[1]);
        }
    }
}

