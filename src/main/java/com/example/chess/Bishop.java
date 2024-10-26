package com.example.chess;

public class Bishop extends SlidingPiece {
    public Bishop(int index, boolean type) {
        super(type,index,"bishop","b");
        this.directions = new int[]{7,-7,9,-9};

    }

    @Override
    public Piece deepCopy(){
        return new Bishop(this.index,this.type);
    }
}
