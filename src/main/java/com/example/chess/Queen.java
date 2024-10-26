package com.example.chess;



public class Queen extends SlidingPiece{
    public Queen(int index, boolean type) {
        super(type,index);
        this.directions = new int[]{1,-1,8,-8,7,-7,9,-9};
        this.name = "queen";
    }


    @Override public Piece deepCopy(){
        return new Queen(this.index,this.type);
    }
}
