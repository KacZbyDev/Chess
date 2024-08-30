package com.example.chess;

import javafx.scene.image.Image;

import java.util.ArrayList;

public abstract class Piece {
    protected int index;
    protected boolean type;
    protected ArrayList<Integer> legalMoves;
    protected boolean isOnStartedSquare;
    public Piece(boolean type, int index) {

        this.type = type;
        this.index = index;
    }
    public int getY() {
        return this.index/8;
    }
    public int getX(){
        return this.index % 8;

    }
    public ArrayList<Integer> getLegalMoves(){
        return legalMoves;
    }

    public boolean getIsOnStartedSquare(){
        return this.isOnStartedSquare;
    }
    public void setIsOnStartedSquare(boolean value){
        this.isOnStartedSquare = value;
    }

    public abstract void setLegalMoves(Piece[] boardRepresentation);

    public abstract Piece deepCopy();



    public void setLegalMoves(){
        this.legalMoves = new ArrayList<>();
    }

    public boolean isLegal(int index){
        for(int move : this.legalMoves){
            if(move  == index){
                return true;
            }
        }
        return false;
    }


    public abstract Image getImage();



}
