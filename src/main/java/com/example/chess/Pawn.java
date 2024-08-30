package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public class Pawn extends Piece{
    int[] captureDirections;
    int moveDirection;
    public Pawn(int index, boolean type) {
        super(type,index);
        this.captureDirections = this.type ? new int[]{-7,-9} : new int[] {9,7};
        this.moveDirection = this.type ? -8:8;
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
    public void setLegalMoves(Piece[] boardRepresentation){
        ArrayList<Integer> moves = new ArrayList<>();
        for(int captureDirection: captureDirections){
            int currentIndex = this.index + captureDirection;
            int directionIndex = FieldToEndOfBoard.DIRECTION.get(captureDirection);
            int boardEdge = FieldToEndOfBoard.FIELDTOENDOFBOARD[this.index][directionIndex];
            if(boardEdge != 0 && boardRepresentation[currentIndex] != null && boardRepresentation[currentIndex].type != this.type){
                moves.add(currentIndex);
            }
        }
        int offset = (this.type && getY()==6) || (!this.type && getY() == 1) ? 2:1;

        for(int i = 0; i<offset; i++){
            int currentIndex = this.index + this.moveDirection * (i+1);
            if(boardRepresentation[currentIndex] == null){
                moves.add(currentIndex);
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

    @Override
    public String toString(){
        return "Pawn";
    }
    @Override public Piece deepCopy(){
        return new Pawn(this.index,this.type);

    }
}
