package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(int index, boolean type) {
        super(type,index);
        this.legalMoves = getLegalMoves();
    }
    public Image getImage() {
        String resourcePath = this.type ? "img/w-knight.png" : "img/b-knight.png";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);

        if (resourceStream == null) {

            System.err.println("Resource not found: " + resourcePath);
            return null;
        }

        return new Image(resourceStream);
    }
    @Override
    public String toString(){

        return "Knight";
    }
    @Override
    public void setLegalMoves(Piece[] boardRepresentation) {
        ArrayList<Integer> moves = new ArrayList<>();
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : knightMoves) {
            int trueRow = getY() + move[0];
            int trueCol = getX() + move[1];

            boolean isInRowRange = trueRow >= 0 && trueRow <= 7;
            boolean isInColRange = trueCol >= 0 && trueCol <= 7;

            if (isInRowRange && isInColRange) {
                int index = 8 * trueRow + trueCol;
                if (boardRepresentation[index] == null || boardRepresentation[index].type != this.type) {
                    moves.add(index);
                }
            }
        }
        this.legalMoves = moves;
    }
    @Override public Piece deepCopy(){
        return new Knight(this.index,this.type);
    }
}
