package com.example.chess;

import java.util.ArrayList;

public abstract class SlidingPiece extends Piece{
    protected int[] directions;
    protected SlidingPiece(boolean type, int index,String name, String fenNotation){
        super(type,index,name,fenNotation);
    }
    @Override
    public void setLegalMoves(BoardState boardState, boolean ignoreKingSafety){
        Piece[] boardRepresentation = boardState.boardRepresentation;
        ArrayList<Integer> moves = new ArrayList<Integer>();
        for(int direction :directions){

            int currentIndex  = this.index+direction;

            for(int i = 0; i < FieldToEndOfBoard.getFields(this.index,direction);i++){
                Piece currentSquare = boardRepresentation[currentIndex];
                if(currentSquare!= null){
                   if(currentSquare.type == this.type){
                       break;
                   }

                    addMove(moves,boardState,this.index,currentIndex, ignoreKingSafety);

                   break;
               }else{
                    addMove(moves,boardState,this.index,currentIndex, ignoreKingSafety);

                   currentIndex+=direction;
                }

            }
        }
        this.legalMoves = moves;
    }
}
