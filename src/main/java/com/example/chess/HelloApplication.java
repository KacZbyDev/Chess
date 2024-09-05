package com.example.chess;

import javafx.application.Application;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;




public class HelloApplication extends Application {


    static StackPane root = new StackPane();

    @Override
    public void start(Stage stage) {
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root);
        Game game = Game.getGameInstance();
        game.launchGame(root);
        primaryStage.setScene(scene);
        setWindowParameters(primaryStage);
        primaryStage.show();
    }
    public void setWindowParameters(Stage primaryStage){
        primaryStage.setTitle("Chess");
        primaryStage.setResizable(false);
        primaryStage.setWidth(654);
        primaryStage.setHeight(677);
    }

    public static void main(String[] args) {
        launch();
    }
}