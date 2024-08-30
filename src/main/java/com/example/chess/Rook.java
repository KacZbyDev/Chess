package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;

public class Rook extends SlidingPiece{

    public Rook(int index, boolean type) {
        super(type,index);
        this.directions = new int[]{1,-1,8,-8};
    }
    @Override
    public Image getImage() {
        String resourcePath = this.type ? "img/w-rook.png" : "img/b-rook.png";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);

        if (resourceStream == null) {

            System.err.println("Resource not found: " + resourcePath);
            return null;
        }

        return new Image(resourceStream);
    }
    @Override
    public String toString(){
        return "Rook";
    }
    @Override public Piece deepCopy(){
        return new Rook(this.index,this.type);
    }
}
