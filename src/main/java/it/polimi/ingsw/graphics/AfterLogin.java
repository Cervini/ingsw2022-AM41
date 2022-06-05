package it.polimi.ingsw.graphics;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AfterLogin {
    @FXML
    private Button confirm;
    @FXML
    private TextField numPlayer;
    @FXML
    private Label wrongNumber;


    public void setPlayersNumber (ActionEvent action) throws IOException {

        String numberOfPlayersString = numPlayer.getText().toString();
        int numberOfPlayers = Integer.parseInt(numberOfPlayersString);
        Main m = new Main();
        if(!numberOfPlayersString.isEmpty()){
            Message request = new Message("start "+numberOfPlayers);
            Message response = GameController.start(request, Main.getClient(),Main.getClient().sameMatchPlayers());
            wrongNumber.setText(response.getArgString());
            //m.changeScene("graphics/fxml/AfterLogin.fxml");
        }
        else {
            wrongNumber.setText("Number not allowed");
        }
    }


}
