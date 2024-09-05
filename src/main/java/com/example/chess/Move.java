package com.example.chess;

public class Move {
    private final int oldIndex;
    private final int newIndex;
    public Move(int oldIndex, int newIndex){
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }
    public int getOldIndex(){
        return this.oldIndex;

    }
    public int getNewIndex(){
        return this.newIndex;
    }
    @Override
    public String toString(){
        return "old index: " + oldIndex +" new index: "+ newIndex;
    }

}
