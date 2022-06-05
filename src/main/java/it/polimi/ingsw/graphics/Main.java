package it.polimi.ingsw.graphics;

import it.polimi.ingsw.server.ClientHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends Application  {

    private static Stage stg;
    private static ClientHandler client;

    public static void main(String[] args, ClientHandler client) {
        Main.client = client;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            stg = primaryStage;
            FXMLLoader loader = new FXMLLoader();
            Parent root =  loader.load(getClass().getClassLoader().getResource("graphics/fxml/Login.fxml").openStream());
            primaryStage.setTitle("Eriantys - Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e ){
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void changeScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent pane =  loader.load(getClass().getClassLoader().getResource(fxml).openStream());
        stg.getScene().setRoot(pane);
    }

    public static ClientHandler getClient() {
        return client;
    }
}
