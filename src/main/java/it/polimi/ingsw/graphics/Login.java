package it.polimi.ingsw.graphics;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.LoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    private Button button;
    @FXML
    private TextField username;
    @FXML
    private Label wrongLogin;

    public void userLogin (ActionEvent action) throws IOException {
        String userId = username.getText().toString();
        Main m = new Main();
        if(! userId.isEmpty()){
            Message request = new Message(userId);
            Message response = LoginController.processLogin(request, Main.getClient());
            wrongLogin.setText(response.getArgString()); //sets label content
            m.changeScene("graphics/fxml/AfterLogin.fxml");
        }
        else {
            wrongLogin.setText("Please, enter your data");
        }
    }

}
