package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

public class MainController {



    public Message ControlTurn(Message request, ClientHandler client) {
        Message output = new Message("string");

        return output;
    }


    public Message ControlPhase(Message request, ClientHandler client){
        Message output = new Message("string");

        return output;
    }
}
