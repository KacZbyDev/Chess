package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public abstract class Piece {
    protected int index;
    protected boolean type;
    protected ArrayList<Integer> legalMoves;
    protected String name;
    private String fenName;
    public Piece(boolean type, int index, String name, String fenName) {
        this.type = type;
        this.index = index;
        this.legalMoves = new ArrayList<>();
        this.name = name;
        this.fenName = fenName;
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


    public abstract void setLegalMoves(BoardState boardState, boolean ignoreKingSafety);

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

    protected boolean isValid(BoardState boardState, int oldIndex, int newIndex){

        BoardState virtualBoardState = new BoardState(boardState);
        virtualBoardState.handleKingPosition(virtualBoardState.getPiece(oldIndex),newIndex);
        virtualBoardState.handleBoardState(oldIndex,newIndex,-1,virtualBoardState.getPiece(oldIndex));
        virtualBoardState.switchTurn();
        virtualBoardState.recalculateLegalMoves(true);
        return !virtualBoardState.isCotrolled(virtualBoardState.getCurrentKingPosition(true));
    }
    protected void addMove(ArrayList <Integer> moves, BoardState boardState, int oldIndex, int newIndex, boolean ignoreKingSafety){
        if(!ignoreKingSafety){
            if(isValid(boardState, oldIndex, newIndex)){
                moves.add(newIndex);
            }
        }
        else{
            moves.add(newIndex);
        }
    }
    public String getFenName(){
        return this.fenName;
    }

    @Override
    public String toString() {
        String color = this.type ? "w" : "b";
        return color+"-"+this.name;
    }

    public Image getImage(){
        String resourcePath = "img/"+ this.toString()+".png";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
        if (resourceStream == null) {

            System.err.println("Resource not found: " + resourcePath);
            return null;
        }

        return new Image(resourceStream);
    }



}
