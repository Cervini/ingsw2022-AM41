package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterController {

    public static Message processChar0(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        LinkedList<Student> chosenStudent = null;
        List studentsIndexes = parameters.getArgNum2();
        try {
            chosenStudent = getStudentsOfCharacter(game,studentsIndexes); //gets students placed on character card
            int islandNumber = parameters.getSingleArgNum2();
            Island chosenIsland = game.getArchipelago().get(islandNumber); //get chosen island
            response.setArgString("Character played successfully");
            game.playCharacter(chosenCharacter, player,chosenStudent,null,chosenIsland,null);
        } catch (NoStudentsException ex) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar1(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        try {
            response.setArgString("Character played successfully");
            game.playCharacter(chosenCharacter, player,null,null,null,null);
        } catch (Exception e) {
            response.setArgString(e.getMessage());
        }
        return response;
    }

    public static Message processChar2(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        int islandNumber = parameters.getSingleArgNum2();
        Island chosenIsland = game.getArchipelago().get(islandNumber);
        try {
            response.setArgString("Character played successfully");
            game.playCharacter(chosenCharacter, player,null,null,chosenIsland,null);
        } catch (Exception e) {
            response.setArgString(e.getMessage());
        }
        return response;
    }

    public static Message processChar3(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
            try {
                game.playCharacter(chosenCharacter, player,null,null,null,null);
                response.setArgString("Character played successfully");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
         return response;
    }

    public static Message processChar4(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        int islandNumber = parameters.getSingleArgNum2();
        Island chosenIsland = game.getArchipelago().get(islandNumber);
        try {
            response.setArgString("Character played successfully");
            game.playCharacter(chosenCharacter, player,null,null,chosenIsland,null);
        } catch (Exception e) {
            response.setArgString(e.getMessage());
        }
        return response;

    }

    public static Message processChar5(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar6(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        try {
            List studentsFromCharacterIndexes = parameters.getArgNum2().stream().limit(3).collect(Collectors.toList());
            List studentsFromEntranceIndexes = parameters.getArgNum2().subList(Math.max(parameters.getArgNum2().size() - 3, 0), parameters.getArgNum2().size());;
            LinkedList<Student> studentsFromCharacter = getStudentsOfCharacter(game,studentsFromCharacterIndexes);
            LinkedList<Student> studentsFromEntrance = getStudentsOfEntrance(player,game, studentsFromEntranceIndexes);
            game.playCharacter(chosenCharacter, player,studentsFromCharacter,studentsFromEntrance,null,null);
            response.setArgString("Character played successfully");
        } catch (NoStudentsException ex ) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar7(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }
    public static Message processChar8(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        Colour chosenColour = parameters.getArgColour();
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,chosenColour);
            response.setArgString("Character played successfully");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar9(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        List studentsFromCharacterIndexes = parameters.getArgNum2().stream().limit(2).collect(Collectors.toList());
        List studentsFromEntranceIndexes = parameters.getArgNum2().subList(Math.max(parameters.getArgNum2().size() - 2, 0), parameters.getArgNum2().size());
        try {
            LinkedList<Student> studentsFromCharacter = getStudentsOfCharacter(game,studentsFromCharacterIndexes);
        } catch (NoStudentsException e) {
            response.setArgString("Not enough students on this card");
        }
        try {
            LinkedList<Student> studentsFromEntrance = getStudentsOfEntrance(player,game, studentsFromEntranceIndexes);
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully");
        } catch (NoStudentsException e) {
            response.setArgString("Not enough students in your entrance");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar10(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        LinkedList<Student> chosenStudent = null;
        List studentsIndexes = parameters.getArgNum2();
        //TODO implement processChar10
        try {
            chosenStudent = getStudentsOfCharacter(game,studentsIndexes); //gets students placed on character card
            game.playCharacter(chosenCharacter, player,chosenStudent,null,null,null);
            response.setArgString("Character played successfully");
        } catch (NoStudentsException ex) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar11(Player player, Message parameters, Game game, Character chosenCharacter){
        Message response = new Message("string");
        Colour chosenColour = parameters.getArgColour();
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,chosenColour);
            response.setArgString("Character played successfully");
        } catch (Exception e) {
            response.setArgString(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    private static LinkedList getStudentsOfCharacter(Game game, List parameters) throws NoStudentsException {
        LinkedList chosenStudent = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudent.add(game.getSelectedCharacters().get(0).getStudents());
        }
        if (game.getSelectedCharacters().get(0).getStudents() == null) throw new NoStudentsException("There no students left");
        return chosenStudent;
    }
    private static LinkedList getStudentsOfEntrance(Player player, Game game, List parameters) throws NoStudentsException {
        LinkedList chosenStudents = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudents.add(player.getSchool().getEntrance().get(i));
        }
        if (game.getSelectedCharacters().get(0).getStudents() == null) throw new NoStudentsException("There no students left");
        return chosenStudents;
    }
    private static LinkedList getStudentsOfDining(Player player,Game game, List parameters) throws NoStudentsException {
        LinkedList chosenStudents = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudents.add(player.getSchool().getEntrance().get(i));
        }
        if (game.getSelectedCharacters().get(0).getStudents() == null) throw new NoStudentsException("There no students left");
        return chosenStudents;
    }
    public static class NoStudentsException extends Exception {
        public NoStudentsException() {}

        public NoStudentsException(String msg) {
            super(msg);
        }
    }


}
