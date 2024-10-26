package com.example.chess;

public class Rook extends SlidingPiece{
    public Rook(int index, boolean type) {
        super(type,index);
        this.directions = new int[]{1,-1,8,-8};
        this.name = "rook";
    }

    @Override public Piece deepCopy(){
        return new Rook(this.index,this.type);
    }
}
