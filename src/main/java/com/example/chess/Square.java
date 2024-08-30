package com.example.chess;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {
    private final int index;
    Color[] precomputedColors;
    boolean isHighlighted;
    Square(int index){
        this.index = index;
        this.precomputedColors = getPrecomputedColors();
    }
    public Color[] getPrecomputedColors(){

        if((getCol()+getRow()) %2 == 0){
            return new Color[]{Color.rgb(238,238,210),Color.rgb(255,74,74),Color.rgb(252,255,77)};
        }
        else{
            return new Color[]{Color.rgb(118,150,86),Color.rgb(255,0,0),Color.rgb(252,255,0)};
        }

    }
    public int getCol(){
        return this.index % 8;
    }
    public int getRow(){
        return this.index / 8;
    }
    public Color getCurrentColor(){
        return isHighlighted ? this.precomputedColors[2] : this.precomputedColors[0];
    }
}
