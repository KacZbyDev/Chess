package com.example.chess;

public class BoardState {
    Piece[] boardRepresentation;
    boolean turn;
    int whiteKingPosition;
    int blackKingPosition;

    public BoardState(BoardState boardState){
        this.boardRepresentation = boardState.getDeepCopy();
        this.turn = boardState.turn;
        this.whiteKingPosition = boardState.whiteKingPosition;
        this.blackKingPosition = boardState.blackKingPosition;
    }
    public BoardState(){
        this.boardRepresentation = new Piece[64];
    }

    public Piece[] getDeepCopy(){
        Piece[] temp = new Piece[this.boardRepresentation.length];
        for(int i = 0; i< this.boardRepresentation.length; i++){
            if(this.boardRepresentation[i] != null){
                temp[i] = this.boardRepresentation[i].deepCopy();
            }
        }
        return temp;

    }
    public void recalculateLegalMoves(boolean ignoreKingSafety){
        for(Piece piece:this.boardRepresentation){
            if(piece!= null){
                if(piece.type == turn){
                    piece.setLegalMoves(this, ignoreKingSafety);
                }
                else{
                    piece.setLegalMoves();
                }
            }
        }
    }
    public void handleKingPosition(Piece piece, int index){
        if(piece instanceof King){
            if(piece.type){
                whiteKingPosition = index;
            }
            else{
                blackKingPosition = index;
            }
        }
    }
    public void handleBoardState(int oldIndex, int newIndex,int captureIndex, Piece piece){
        piece.index = newIndex;
        if(captureIndex != -1) boardRepresentation[captureIndex] = null;
        boardRepresentation[newIndex] = piece;
        boardRepresentation[oldIndex] = null;
    }
    public Piece getPiece(int index){
        if(index <= 63 && index >= 0) return boardRepresentation[index];
        throw new ArrayIndexOutOfBoundsException("Index is out of bounds");
    }
    public boolean isCotrolled(int position) {
        for (Piece piece : this.boardRepresentation) {
            if (piece != null && canCapture(piece, position)) {
                return true;
            }
        }
        return false;
    }
    private boolean canCapture(Piece piece, int position) {
        for (int move : piece.getLegalMoves()) {
            if (piece instanceof Pawn pawn && isPawnCapturingKing(pawn,move,position)) {
                return true;
            }
            if (move == position) {
                return true;
            }
        }
        return false;
    }

    private boolean isPawnCapturingKing(Pawn pawn, int move, int position) {
        for (int direction : pawn.captureDirections) {
            if (move == position && pawn.index - direction == position) {

                return true;
            }
        }
        return false;
    }
    public int getCurrentKingPosition(){
        return !turn ? whiteKingPosition : blackKingPosition;
    }
    public boolean isAnyLegalMoves(){
        for(Piece piece: boardRepresentation){
            if(piece != null && piece.type == turn && !piece.getLegalMoves().isEmpty()){
                return true;
            }

        }return false;
    }

}
