package com.example.chess;
import java.util.HashMap;

public class PieceFactory {
    public static final HashMap<Character,TriFunction<Integer,Boolean,Piece>> map = getHashMap();
    public static Piece getPieceInstance(Character letter,int index){
        boolean type = Character.isUpperCase(letter);

        return map.get(Character.toUpperCase(letter)).apply(index,type);
    }

    public static HashMap<Character,TriFunction<Integer,Boolean,Piece>> getHashMap(){
        HashMap<Character,TriFunction<Integer,Boolean,Piece>> map = new HashMap<>();
        map.put('Q',Queen::new);
        map.put('K',King::new);
        map.put('B',Bishop::new);
        map.put('P',Pawn::new);
        map.put('N',Knight::new);
        map.put('R',Rook::new);
        return map;
    }
}
