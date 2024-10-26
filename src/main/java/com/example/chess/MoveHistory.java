package com.example.chess;

import java.util.HashMap;

public class MoveHistory {
    private HashMap<String,Integer> positionTracker;

    public MoveHistory() {
        this.positionTracker = new HashMap<>();
    }

    public void handlePositionOccurrence(String position){
        positionTracker.merge(position, 1, Integer::sum);

    }
    public String getFenFromBoardState(BoardState boardState){
        StringBuilder fen = new StringBuilder();
        Piece[] boardRepresentation = boardState.boardRepresentation;
        int emptySquareCount = 0;
        for (int i = 0; i < boardRepresentation.length; i++) {
            if(i % 8 == 0 && i !=0){
                if(emptySquareCount != 0) fen.append(emptySquareCount);
                emptySquareCount = 0;
                fen.append("/");

            }
            Piece curPiece = boardRepresentation[i];
            if(curPiece != null){
                if(emptySquareCount != 0) fen.append(emptySquareCount);
                emptySquareCount = 0;
                String currentPieceFen = curPiece.getFenName();
                if(curPiece.type) currentPieceFen = currentPieceFen.toUpperCase();
                fen.append(currentPieceFen);


            }else{
                emptySquareCount++;
            }
        }
        if(emptySquareCount != 0) fen.append(emptySquareCount);
        return fen.toString();
    }
    public int getOccurrence(String position){
        return positionTracker.get(position);
    }

}
