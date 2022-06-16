package it.polimi.ingsw.graphics;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Startup extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        Image icon = new Image("graphics/others/logo.png");
        Image backgroundImage = new Image("graphics/others/eriantysMainPicture.jpg");
        ImageView imageView = new ImageView(backgroundImage);

        primaryStage.setTitle("Eriantys");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);

        root.getChildren().add(imageView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //--module-path D:\openjfx-18.0.1_windows-x64_bin-sdk\javafx-sdk-18.0.1\lib --add-modules javafx.controls,javafx.fxml
}
