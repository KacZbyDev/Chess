package com.example.chess;


import static com.example.chess.Board.SQUARE_SIZE;

import static com.example.chess.Game.pieceGroup;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoardStateUiUpdater {
    int[] imageToDelete;
    Piece[] imageToCreate = new Piece[2];

    BoardStateUiUpdater(){
        this.imageToDelete = new int[] {-1,-1};
    }


    public void deleteImageViews(){
        for(int i =0;i<imageToDelete.length; i++){
            Node imageToDelete = pieceGroup.lookup("#" + this.imageToDelete[i]);
            pieceGroup.getChildren().remove(imageToDelete);
            this.imageToDelete[i] = -1;
        }
    }
    public void createImageView(){
        for(int i = 0; i<imageToCreate.length;i++){
            if(imageToCreate[i] != null){
                setImageViewParamaters(imageToCreate[i]);
            }

            imageToCreate[i] = null;
        }
    }
    public void setImageViewParameters(Piece piece) {
        Image image = piece.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setId(String.valueOf(piece.index));
        imageView.setX(piece.getX() * SQUARE_SIZE);
        imageView.setY(piece.getY() * SQUARE_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(SQUARE_SIZE);
        imageView.setFitWidth(SQUARE_SIZE);
        Game.getGameInstance().makeDraggable(imageView);
        Game.pieceGroup.getChildren().add(imageView);
    }
    public void setUiUpdate(int index, int delete, Piece create){
        imageToDelete[index] = delete;
        imageToCreate[index] = create;
    }
}
