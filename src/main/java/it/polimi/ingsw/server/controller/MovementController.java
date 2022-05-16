package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSession;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.server.ClientHandler;

public class MovementController {

    public static Message place(Message request, ClientHandler client) {
        Player current_player = client.getGame().getPlayer(client.getUsername());
        Message output = new Message("string");

        try  {
            output = new Message("string");
            if (request.getArgNum1() >= current_player.getSchool().getEntranceSize()) {
                output.setArgString("Non existing student, please retry");
            } else {

                Student played = current_player.getSchool().getEntrance().get(request.getArgNum1());
                current_player.getSchool().removeStudent(played); //removes student from entrance

                switch (request.getTo_tile()){
                    case DINING -> current_player.getSchool().getDining_rooms().get(request.getArgNum2()).putStudent(played);
                    case ISLAND -> client.getGame().getArchipelago().get(request.getArgNum2()).putStudent(played);
                }
                output.setArgString("Student placed");

            }
        } catch (Exception e) {
            output.setArgString("There are no more seats in the dining room, please retry ");
        }


        return output;
    }

    public static Message move(Message request, ClientHandler client) {
        Message output = new Message("string");
        Game current_game = client.getGame();
        Player current_player = client.getGame().getPlayer(client.getUsername());
        int mother_nature_movements = request.getArgNum1();
        try {
            current_game.moveMotherNature(mother_nature_movements,current_player);
        } catch (Exception e) {
            output.setArgString("Can't move Mother Nature this far, please retry");
        }
        return output;
    }



}
