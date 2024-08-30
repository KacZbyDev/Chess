package com.example.chess;

import java.util.HashMap;

public class FieldToEndOfBoard {
    public final static HashMap<Integer, Integer> DIRECTION= setDirections();
    public final static int[][] FIELDTOENDOFBOARD = setFieldToEndArray();

    private FieldToEndOfBoard(){

    }

    public static int[][] setFieldToEndArray() {
        int[][] array = new int[64][8];
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int index = row * 8 + column;
                int numWest = column;
                int numEast = 7 - column;
                int numNorth = row;
                int numSouth = 7 - row;


                int numNorthWest = Math.min(numNorth, numWest);
                int numNorthEast = Math.min(numNorth, numEast);
                int numSouthWest = Math.min(numSouth, numWest);
                int numSouthEast = Math.min(numSouth, numEast);


                array[index] = new int[]{
                        numEast,
                        numWest,
                        numSouth,
                        numNorth,
                        numSouthWest,
                        numNorthEast,
                        numSouthEast,
                        numNorthWest
                };
            }
        }
        return array;
    }

    public static HashMap<Integer,Integer> setDirections() {
        HashMap<Integer, Integer> directions = new HashMap<>();
        directions.put(1, 0);
        directions.put(-1, 1);
        directions.put(8, 2);
        directions.put(-8, 3);
        directions.put(7, 4);
        directions.put(-7, 5);
        directions.put(9, 6);
        directions.put(-9, 7);

        return directions;
    }
    public static int getDirectionIndex(int direction){
        return DIRECTION.get(direction);
    }


}
