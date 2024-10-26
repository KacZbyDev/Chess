package com.example.chess;

public class Rook extends SlidingPiece{
    public Rook(int index, boolean type) {
        super(type,index,"rook","r");
        this.directions = new int[]{1,-1,8,-8};

    }

    @Override public Piece deepCopy(){
        return new Rook(this.index,this.type);
    }
}
