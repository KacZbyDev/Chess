package com.example.chess;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.chess.Board.SQUARE_SIZE;

public class Game {

    public static Game gameInstance;
    public final ArrayList<Move> movesHistory = new ArrayList<>();
    private BoardState boardState;
    private final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final Group pieceGroup = new Group();
    public static final Group boardGroup = new Group();
    private final Board board = new Board();
    private final BoardStateUiUpdater boardStateUiUpdater = new BoardStateUiUpdater();
    private boolean isClicked;

    private Game() {
        this.boardState = new BoardState();
    }

    public static Game getGameInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    public void launchGame(StackPane root) {
        boardState.boardRepresentation = getPieceRepresentation(STARTING_POSITION_FEN);
        boardState.turn = STARTING_POSITION_FEN.split(" ")[1].equals("w");
        boardGroup.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                board.unHighlight(boardGroup.getChildren());
            }
        });
        board.initializeBoard(boardGroup);
        initializePieces();
        root.getChildren().addAll(boardGroup, pieceGroup);
    }

    public Piece[] getPieceRepresentation(String fen) {
        Piece[] result = new Piece[64];
        HashMap<Character, TriFunction<Integer, Boolean, Piece>> map = PieceFactory.map;
        String[] splitFen = fen.split(" ");
        int index = 0;

        for (char letter : splitFen[0].toCharArray()) {
            if (letter == '/') {
                continue;
            }
            if (Character.isDigit(letter)) {
                index += Character.getNumericValue(letter);
            } else {
                if (map.containsKey(Character.toUpperCase(letter))) {
                    result[index] = map.get(Character.toUpperCase(letter)).apply(index, Character.isUpperCase(letter));
                    index++;
                }
            }
        }

        return result;
    }

    public void initializePieces() {
        for (Piece piece : boardState.boardRepresentation) {
            if (piece != null) {
                if (boardState.turn == piece.type) {
                    piece.setLegalMoves(boardState, false);
                } else {
                    piece.setLegalMoves();
                }
                boardState.handleKingPosition(piece, piece.index);
                boardStateUiUpdater.setImageViewParameters(piece);

            }
            String position = boardState.getFen();
            boardState.moveHistory.handlePositionOccurrence(position);
            boardState.moveHistory.getOccurrence(position);
        }
    }


    public void makeDraggable(ImageView imageView) {
        int index = Integer.parseInt(imageView.getId());
        Piece piece = boardState.getPiece(index);

        imageView.setOnMouseEntered(_ -> {
            if (piece.type == boardState.turn) {
                imageView.setCursor(Cursor.HAND);
            }
        });

        imageView.setOnMouseReleased(_ -> {
            if (piece.type == boardState.turn) {
                imageView.setCursor(Cursor.HAND);
                imageView.toBack();
                gameLoopHandler(imageView);
                board.unHighlight(boardGroup.getChildren());
                isClicked = false;
            }
        });

        imageView.setOnMouseDragged(event -> {
            if (piece.type == boardState.turn && event.getButton() == MouseButton.PRIMARY) {
                if (!isClicked) {
                    board.unHighlight(boardGroup.getChildren());
                    int id = Integer.parseInt(imageView.getId());
                    Piece movingPiece = boardState.getPiece(id);
                    board.highlightLegalMoves(movingPiece);
                    isClicked = !isClicked;
                }
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
        });

        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Square standingSquare = (Square) boardGroup.lookup("#" + index + "r");
                Board.getNextColor(standingSquare);
            }
        });
    }
    private void gameLoopHandler(ImageView imageView) {
        int captureIndex;
        Piece newPiece;
        double row = Math.round(imageView.getX() / SQUARE_SIZE);
        double column = Math.round(imageView.getY() / SQUARE_SIZE);

        int oldIndex = Integer.parseInt(imageView.getId());
        int newIndex = (int) column * 8 + (int) row;

        Move move = new Move(oldIndex, newIndex);
        Piece movingPiece = boardState.getPiece(oldIndex);
        if (movingPiece == null) {
            resetImageViewPosition(imageView, oldIndex);
            return;
        }

        if (movingPiece.isLegal(move.getNewIndex())) {
            BoardState boardShot = new BoardState(boardState);

            if (movingPiece instanceof Pawn && ((Pawn) movingPiece).isInBackRank(column)) {
                newPiece = new Queen(newIndex, movingPiece.type);
            } else {
                newPiece = movingPiece;
                newPiece.index = newIndex;
            }

            if (newPiece instanceof Pawn pawn && pawn.isCapturingEnPassant(move.getNewIndex(), boardShot.boardRepresentation)) {
                captureIndex = move.getNewIndex() + pawn.moveDirection * -1;
            } else {
                captureIndex = move.getNewIndex();
            }

            boardStateUiUpdater.setUiUpdate(1, captureIndex, null);
            boardShot.handleBoardState(oldIndex, newIndex, captureIndex, newPiece);
            boardStateUiUpdater.setUiUpdate(0, move.getOldIndex(), newPiece);
            boardShot.handleKingPosition(boardShot.getPiece(newIndex), newIndex);

            if (newPiece instanceof King king && king.isCastling(oldIndex, newIndex)) {
                int castleDirection = king.getCastleDirection(oldIndex, newIndex);
                int directionIndex = FieldToEndOfBoard.getDirectionIndex(castleDirection);
                int oldRookPosition = move.getOldIndex() + FieldToEndOfBoard.FIELDTOENDOFBOARD[move.getOldIndex()][directionIndex] * castleDirection;
                int newRookPosition = move.getNewIndex() + castleDirection * -1;

                boardShot.handleBoardState(oldRookPosition, newRookPosition, -1, boardShot.boardRepresentation[oldRookPosition]);
                boardStateUiUpdater.setUiUpdate(1, oldRookPosition, boardShot.boardRepresentation[newRookPosition]);
            }

            boardShot.switchTurn();
            movesHistory.add(move);
            boardShot.recalculateLegalMoves(false);
            boardState = boardShot;
            if(boardShot.isThreefoldRepetition()){
                displayEndGameMessage("draw by threefold repetition");
            }

            if (!boardState.isAnyLegalMoves()) {
                boardState.switchTurn();
                boardState.recalculateLegalMoves(true);
                boolean isInCheck = boardState.isCotrolled(boardState.getCurrentKingPosition(true));
                String message = isInCheck ? "Checkmate": "Stalemate";
                displayEndGameMessage(message);
            }
            boardStateUiUpdater.deleteImageViews();
            boardStateUiUpdater.createImageView();
            board.updateHighlightedSquares(move.getOldIndex(), move.getNewIndex());

        } else {
            resetImageViewPosition(imageView, move.getOldIndex());
        }
    }
    private void displayEndGameMessage(String message) {
        Text text = new Text(message);
        Stage stage = (Stage) boardGroup.getScene().getWindow();
        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        root.getChildren().add(text);
        stage.setScene(scene);
    }

    private void resetImageViewPosition(ImageView imageView, int oldIndex) {
        imageView.setX((oldIndex % 8) * SQUARE_SIZE);
        imageView.setY((oldIndex / 8) * SQUARE_SIZE);
    }
}