package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;

public class Queen extends SlidingPiece{

    public Queen(int index, boolean type) {
        super(type,index);
        this.directions = new int[]{1,-1,8,-8,7,-7,9,-9};
    }
    @Override
    public Image getImage() {
        String resourcePath = this.type ? "img/w-queen.png" : "img/b-queen.png";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);

        if (resourceStream == null) {

            System.err.println("Resource not found: " + resourcePath);
            return null;
        }

        return new Image(resourceStream);
    }
    @Override
    public String toString(){
        return "Queen";
    }
    @Override public Piece deepCopy(){
        return new Queen(this.index,this.type);
    }
}
