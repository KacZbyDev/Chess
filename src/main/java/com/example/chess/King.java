package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public class King extends Piece{
    private final int[] directions = new int[]{1,-1,8,-8,-7,7,9,-9};
    private final int[] castleDirections= new int[]{1,-1};
    private final int startingSquare= this.type ? 60:4;
    public King(int index, boolean type) {
        super(type,index);
        this.name = "king";
    }

    @Override
    public void setLegalMoves(BoardState boardState, boolean ignoreKingSafety) {
        Piece[] boardRepresentation = boardState.boardRepresentation;
        ArrayList<Integer> moves = new ArrayList<>();
        for (int direction : directions){
            int currentIndex = this.index + direction;

            int toEndOfTheBoard = FieldToEndOfBoard.getFields(this.index,direction);
            if(toEndOfTheBoard != 0 && (boardRepresentation[currentIndex] == null || boardRepresentation[currentIndex].type != this.type)){
                addMove(moves,boardState,this.index,currentIndex, ignoreKingSafety);

            }
        }
        this.legalMoves = moves;
        castleHandler(boardState);

    }
    private void castleHandler(BoardState boardState){

        if(this.index == startingSquare && this.isFirstMove()){
            Piece[] boardRepresentation = boardState.boardRepresentation;
            for(int direction :castleDirections){
                int currentIndex  = this.index+direction;
                int directionIndex = FieldToEndOfBoard.DIRECTION.get(direction);
                int iterationCount = FieldToEndOfBoard.FIELDTOENDOFBOARD[this.index][directionIndex];
                for(int i = 0; i < iterationCount;i++){
                    if(iterationCount - i ==1 && boardRepresentation[currentIndex] instanceof Rook rook && rook.isFirstMove()){
                        if(canCastle(direction,boardState)) this.legalMoves.add(this.index + 2*direction);
                    }
                    if(boardRepresentation[currentIndex] != null){
                        break;
                    }
                    currentIndex+=direction;

                }
            }
        }

    }

    public boolean canCastle(int direction,BoardState boardState){
        for (int i = 0; i <= 2; i++) {
            int castleIndex = this.index + direction * i;
            if(!isValid(boardState,this.index,castleIndex)){

                return false;
            }
        }return true;
    }

    public boolean isCastling(int position, int move){

        return Math.abs(position-move) ==2;
    }
    @Override
    public Piece deepCopy(){
        return new King(this.index,this.type);
    }
    public int getCastleDirection(int position,int move){
        return position-move ==2 ? -1:1;
    }

}
