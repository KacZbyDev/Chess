package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public class Pawn extends Piece{
    int[] captureDirections;
    int moveDirection;
    int enPassantRank;
    public Pawn(int index, boolean type) {
        super(type,index);
        this.captureDirections = this.type ? new int[]{-7,-9} : new int[] {9,7};
        this.moveDirection = this.type ? -8:8;
        this.enPassantRank = this.type ? 3:5;
    }
    @Override
    public Image getImage() {
        String resourcePath = this.type ? "img/w-pawn.png" : "img/b-pawn.png";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);

        if (resourceStream == null) {

            System.err.println("Resource not found: " + resourcePath);
            return null;
        }

        return new Image(resourceStream);
    }
    @Override
    public void setLegalMoves(BoardState boardState, boolean ignoreKingSafety){
        Piece[] boardRepresentation = boardState.boardRepresentation;
        ArrayList<Integer> moves = new ArrayList<>();
        for(int captureDirection: captureDirections){
            int currentIndex = this.index + captureDirection;
            int boardEdge = FieldToEndOfBoard.getFields(this.index,captureDirection);
            boolean isEnPassantValid = validateEnPassant(boardRepresentation,this.index, captureDirection);
            if((boardEdge != 0 && boardRepresentation[currentIndex] != null && boardRepresentation[currentIndex].type != this.type) || isEnPassantValid){
                addMove(moves,boardState,this.index,currentIndex, ignoreKingSafety);
            }
        }
        int offset = (this.type && getY()==6) || (!this.type && getY() == 1) ? 2:1;

        for(int i = 0; i<offset; i++){
            int currentIndex = this.index + this.moveDirection * (i+1);
            if(boardRepresentation[currentIndex] == null){
                addMove(moves,boardState,this.index,currentIndex, ignoreKingSafety);
            }
            else{
                break;
            }

        }



        this.legalMoves = moves;
    }
    public boolean isInBackRank(double row){

        return (this.type && row ==0) || (!this.type && row == 7);
    }
    public boolean validateEnPassant(Piece[] boardRepresentation, int position,int captureDirection){
        Move lastMove;
        int enPassantDirection = captureDirection == -9 || captureDirection == 7? -1:1;
        int indexDifference = captureDirection >0?16:-16;
        if(Game.getGameInstance().movesHistory.isEmpty()){
            return false;
        }else{
            lastMove = Game.getGameInstance().movesHistory.getLast();
        }

        return boardRepresentation[lastMove.getNewIndex()] instanceof Pawn && position + enPassantDirection == lastMove.getNewIndex() && lastMove.getNewIndex()+indexDifference == lastMove.getOldIndex();


    }
    public boolean isCapturingEnPassant(int position, Piece[] boardRepresentation){
        return position % 8 != getX() && boardRepresentation[position] == null;
    }

    @Override
    public String toString(){
        return "Pawn";
    }
    @Override public Piece deepCopy(){
        return new Pawn(this.index,this.type);

    }
}
