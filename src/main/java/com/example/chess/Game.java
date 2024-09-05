package com.example.chess;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import static com.example.chess.Board.SQUARE_SIZE;

public class Game {

    private static Game gameInstance;
    ArrayList<Move> movesHistory = new ArrayList<>();
    Boolean turn;
    Piece[] boardRepresentation;
    final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    Group pieceGroup = new Group();
    static Group boardGroup = new Group();
    Board board = new Board();
    int whiteKingPosition;
    int blackKingPosition;


    private Game(){

    }
    public static Game getGameInstance(){
        if(gameInstance == null){
            gameInstance = new Game();
        }
        return gameInstance;
    }
    public void launchGame(StackPane root){
        boardRepresentation = getPieceRepresentation(STARTING_POSITION_FEN);
        boardGroup.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                board.unHighlight(boardGroup.getChildren());
            }
        });
        board.initializeBoard(boardGroup);
        initializePieces(boardRepresentation);
        root.getChildren().addAll(boardGroup,pieceGroup);
    }

    public Piece[] getPieceRepresentation(String fen) {
        Piece[] result = new Piece[64];
        HashMap<Character, TriFunction<Integer, Boolean, Piece>> map = PieceFactory.map;
        String[] splitFen = fen.split(" ");
        int index = 0;

        for (char letter : splitFen[0].toCharArray()) {
            if (letter != '/') {
                if (Character.isDigit(letter)) {
                    index += Character.getNumericValue(letter);
                } else {
                    if (map.containsKey(Character.toUpperCase(letter))) {
                        result[index] = PieceFactory.getPieceInstance(letter,index);
                        handleKingPosition(result[index], index);
                        index++;
                    }
                    else{
                        throw new IllegalArgumentException("invalid FEN string");
                    }

                }

            }
        }
        turn = splitFen[1].equals("w");
        return result;
    }

    public void initializePieces(Piece[] boardRepresentation) {
        for (Piece piece : boardRepresentation) {
             if(piece != null) {
                 if(turn == piece.type){
                     piece.setLegalMoves(boardRepresentation);
                 }
                 setImageViewParamaters(piece);
             }
        }
    }

    private void makeDraggable(ImageView imageView) {
        int index = Integer.parseInt(imageView.getId());
        Piece piece = boardRepresentation[index];

        imageView.setOnMouseEntered( _ -> {
            if(piece.type == turn){
                imageView.setCursor(Cursor.HAND);
            }

        });
        imageView.setOnMouseReleased( _ -> {
            if(piece.type == turn){
                imageView.setCursor(Cursor.HAND);
                imageView.toBack();
                gameLoopHandler(imageView);
            }


        });
        imageView.setOnMouseDragged(event -> {
            if(piece.type == turn){
                if(event.getButton() == MouseButton.PRIMARY){
                    board.unHighlight(boardGroup.getChildren());
                    double newX = event.getSceneX() - 40;
                    double newY = event.getSceneY() - 40;
                    imageView.toFront();
                    if (newX >= 0 && newX <= (Board.BOARD_SIZE - 1) * SQUARE_SIZE) {
                        imageView.setX(newX);
                    }
                    if (newY >= 0 && newY <= (Board.BOARD_SIZE - 1) * SQUARE_SIZE) {
                        imageView.setY(newY);
                    }
                }

            }

        });

    }

    private void gameLoopHandler(ImageView imageView) {
        boolean isPromoted = false;
        boolean isCastled = false;
        int captureIndex;
        int beforeCastleRookPosition = 0;
        int afterCastleRookPosition = 0;
        Piece newPiece;
        double row = Math.round(imageView.getX() / SQUARE_SIZE);
        double column = Math.round(imageView.getY() / SQUARE_SIZE);

        Move move = new Move(Integer.parseInt(imageView.getId()), (int) column * 8 + (int) row);

        if (boardRepresentation[move.getOldIndex()].isLegal(move.getNewIndex())) {
            Piece[] boardShot = getDeepCopy(boardRepresentation);
            Piece oldPosition = boardShot[move.getOldIndex()];


            if (oldPosition instanceof Pawn && ((Pawn) oldPosition).isInBackRank(column)) {
                newPiece = new Queen(oldPosition.index, oldPosition.type);
                isPromoted = true;
            } else {
                newPiece = oldPosition;
            }
            if(newPiece instanceof Pawn pawn && pawn.isCapturingEnPassant(move.getNewIndex(), boardShot)){
                captureIndex = move.getNewIndex() + pawn.moveDirection*-1;
            }else{
                captureIndex = move.getNewIndex();
            }

            handleBoardState(boardShot, move.getOldIndex(), move.getNewIndex(),captureIndex, newPiece);

            handleKingPosition(boardShot[move.getNewIndex()], move.getNewIndex());
            if (newPiece instanceof King king && king.isCastling(move.getOldIndex(), move.getNewIndex())) {
                isCastled = true;
                int castleDirection = king.getCastleDirection(move.getOldIndex(), move.getNewIndex());
                int directionIndex = FieldToEndOfBoard.getDirectionIndex(castleDirection);
                int oldRookPosition = move.getOldIndex() + FieldToEndOfBoard.FIELDTOENDOFBOARD[move.getOldIndex()][directionIndex] * castleDirection;
                int newRookPosition = move.getNewIndex() + castleDirection * -1;


                handleBoardState(boardShot, oldRookPosition, newRookPosition,captureIndex, boardShot[oldRookPosition]);

                afterCastleRookPosition = newRookPosition;
                beforeCastleRookPosition = oldRookPosition;
            }

            turn = !turn;
            int currentKingPosition = turn ? blackKingPosition : whiteKingPosition;
            movesHistory.add(move);
            recalculateLegalMoves(boardShot);


            if (!King.isControled(currentKingPosition, boardShot)) {
                boardRepresentation = boardShot;

                Node imageToDelete = pieceGroup.lookup("#" + captureIndex);
                pieceGroup.getChildren().remove(imageToDelete);
                imageView.setId(String.valueOf(move.getNewIndex()));
                updateHighlightedSquares(move.getOldIndex(), move.getNewIndex());

                if (isPromoted) {
                    pieceGroup.getChildren().remove(imageView);
                    setImageViewParamaters(newPiece);
                } else {
                    updateUiPosition(imageView, column, row);
                }

                if (isCastled) {
                    Piece castledRook = boardRepresentation[afterCastleRookPosition];
                    ImageView rookImageView = (ImageView) pieceGroup.lookup("#" + beforeCastleRookPosition);
                    rookImageView.setId(String.valueOf(afterCastleRookPosition));
                    updateUiPosition(rookImageView, castledRook.getY(), castledRook.getX());
                }
            } else {
                turn = !turn;
                movesHistory.removeLast();
                resetImageViewPosition(imageView, move.getOldIndex());
            }
        } else {
            resetImageViewPosition(imageView, move.getOldIndex());
        }
    }

    private void updateHighlightedSquares(int oldIndex, int newIndex) {
        for (Square square : board.highlightedSquares) {
            if (square != null) {
                square.isHighlighted = false;
                square.setFill(square.precomputedColors[0]);
            }
        }
        Square previousSquare = (Square) boardGroup.lookup("#" + oldIndex + "r");
        Square currentSquare = (Square) boardGroup.lookup("#" + newIndex + "r");
        previousSquare.isHighlighted = true;
        currentSquare.isHighlighted = true;
        previousSquare.setFill(previousSquare.precomputedColors[2]);
        currentSquare.setFill(currentSquare.precomputedColors[2]);
        board.highlightedSquares[0] = previousSquare;
        board.highlightedSquares[1] = currentSquare;
    }


    private void setImageViewParamaters(Piece piece) {
        Image image = piece.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setId(String.valueOf(piece.index));
        imageView.setX(piece.getX() * SQUARE_SIZE);
        imageView.setY(piece.getY() * SQUARE_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(SQUARE_SIZE);
        imageView.setFitWidth(SQUARE_SIZE);
        makeDraggable(imageView);
        pieceGroup.getChildren().add(imageView);
    }
    private void recalculateLegalMoves(Piece[] board){
        for(Piece piece:board){
            if(piece!= null){
                if(piece.type == turn){
                    piece.setLegalMoves(board);
                }
                else{
                    piece.setLegalMoves();
                }
            }
        }
    }
    private void handleKingPosition(Piece piece, int index){
        if(piece instanceof King){
            if(piece.type){
                whiteKingPosition = index;
            }
            else{
                blackKingPosition = index;
            }
        }
    }
    private Piece[] getDeepCopy(Piece[] board){
        Piece[] temp = new Piece[board.length];
        for(int i = 0; i< board.length; i++){
            if(board[i] != null){
                temp[i] = board[i].deepCopy();
            }
        }
        return temp;

    }
    private void resetImageViewPosition(ImageView imageView, int oldIndex){
        imageView.setX((oldIndex % 8) * SQUARE_SIZE);
        imageView.setY((oldIndex / 8) * SQUARE_SIZE);
    }
    private void handleBoardState(Piece[] boardShot, int oldIndex, int newIndex,int captureIndex, Piece piece){
        piece.index = newIndex;
        boardShot[captureIndex] = null;
        boardShot[newIndex] = piece;
        boardShot[oldIndex] = null;
    }
    private void updateUiPosition(ImageView imageView, double col, double row){
        imageView.setX(row * SQUARE_SIZE);
        imageView.setY(col * SQUARE_SIZE);
    }

}




