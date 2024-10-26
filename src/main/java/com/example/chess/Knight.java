package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(int index, boolean type) {
        super(type,index,"knight","n");
        this.legalMoves = getLegalMoves();
    }

    @Override
    public void setLegalMoves(BoardState boardState, boolean ignoreKingSafety) {
        Piece[] boardRepresentation = boardState.boardRepresentation;
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
                    addMove(moves,boardState,this.index,index, ignoreKingSafety);
                }
            }
        }
        this.legalMoves = moves;

    }
    @Override public Piece deepCopy(){
        return new Knight(this.index,this.type);
    }
}
