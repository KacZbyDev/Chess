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


    public abstract void setLegalMoves(Piece[] boardRepresentation);

    public abstract Piece deepCopy();

    public boolean isFirstMove(){
        ArrayList<Move> moves = Game.getGameInstance().movesHistory;
        for(int i = moves.size()-1; i>=0;i--){
            if(moves.get(i).getNewIndex() == this.index){
                return false;
            }
        }
        return true;
    }

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
