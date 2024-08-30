package com.example.chess;

import javafx.scene.image.Image;

import java.io.InputStream;

public class Bishop extends SlidingPiece {

    public Bishop(int index, boolean type) {
        super(type,index);
        this.directions = new int[]{7,-7,9,-9};
    }
    @Override
    public Image getImage() {
        String resourcePath = this.type ? "img/w-bishop.png" : "img/b-bishop.png";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);

        if (resourceStream == null) {

            System.err.println("Resource not found: " + resourcePath);
            return null;
        }

        return new Image(resourceStream);
    }
    @Override
    public String toString(){
        return "Bishop";
    }
    @Override
    public Piece deepCopy(){
        return new Bishop(this.index,this.type);
    }
}
