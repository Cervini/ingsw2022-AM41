package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterController {

    public static Message processChar0(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        LinkedList<Student> chosenStudent = null;
        List<Integer> studentsIndexes = parameters.getArgNum2();
        try {
            chosenStudent = getStudentsOfCharacter(chosenCharacter,game,studentsIndexes); //gets students placed on character card
            int islandNumber = parameters.getSingleArgNum2();
            Island chosenIsland = game.getArchipelago().get(islandNumber); //get chosen island
            response.setArgString("Character played successfully, student placed on island "+islandNumber);
            game.playCharacter(chosenCharacter, player,chosenStudent,null,chosenIsland,null);
        } catch (NoStudentsException ex) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar1(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        try {
            response.setArgString("Character played successfully, you have taken control of all professors");
            game.playCharacter(chosenCharacter, player,null,null,null,null);
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar2(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        int islandNumber = parameters.getSingleArgNum2();
        Island chosenIsland = game.getArchipelago().get(islandNumber);
        try {
            response.setArgString("Character played successfully, mother nature has been placed on island "+islandNumber);
            game.playCharacter(chosenCharacter, player,null,null,chosenIsland,null);
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar3(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
            try {
                game.playCharacter(chosenCharacter, player,null,null,null,null);
                response.setArgString("Character played successfully, you may move Mother Nature up to 2 additional Islands");
        } catch (Exception e) {
                throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
         return response;
    }

    public static Message processChar4(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        int islandNumber = parameters.getSingleArgNum2();
        Island chosenIsland = game.getArchipelago().get(islandNumber);
        try {
            response.setArgString("Character played successfully, no-entry tile has been placed on island "+islandNumber);
            game.playCharacter(chosenCharacter, player,null,null,chosenIsland,null);
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;

    }

    public static Message processChar5(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully, next time mother nature will move towers will not count towards influence ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar6(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        try {
            List<Integer> studentsFromCharacterIndexes = parameters.getArgNum2().stream().limit(3).collect(Collectors.toList());
            List<Integer> studentsFromEntranceIndexes = parameters.getArgNum2().subList(Math.max(parameters.getArgNum2().size() - 3, 0), parameters.getArgNum2().size());;
            LinkedList<Student> studentsFromCharacter = getStudentsOfCharacter(chosenCharacter, game,studentsFromCharacterIndexes);
            LinkedList<Student> studentsFromEntrance = getStudentsOfEntrance(chosenCharacter,player,game, studentsFromEntranceIndexes);
            game.playCharacter(chosenCharacter, player,studentsFromCharacter,studentsFromEntrance,null,null);
            response.setArgString("Character played successfully, students have been placed in your School board Entrance");
        } catch (NoStudentsException ex ) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar7(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully, you have 2 additional influence points ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }
    public static Message processChar8(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        Colour chosenColour = parameters.getStandardArgColour();
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,chosenColour);
            response.setArgString("Character played successfully, this colour will add no influence ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar9(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        List<Integer> studentsFromCharacterIndexes = parameters.getArgNum2().stream().limit(2).collect(Collectors.toList());
        List<Integer> studentsFromEntranceIndexes = parameters.getArgNum2().subList(Math.max(parameters.getArgNum2().size() - 2, 0), parameters.getArgNum2().size());
        try {
            LinkedList<Student> studentsFromCharacter = getStudentsOfCharacter(chosenCharacter, game,studentsFromCharacterIndexes);
        } catch (NoStudentsException e) {
            response.setArgString("Not enough students on this card");
        }
        try {
            LinkedList<Student> studentsFromEntrance = getStudentsOfEntrance(chosenCharacter, player,game, studentsFromEntranceIndexes);
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully, students have been exchanged!");
        } catch (NoStudentsException e) {
            response.setArgString("Not enough students in your entrance");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar10(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        LinkedList<Student> chosenStudent = null;
        List<Integer> studentsIndexes = parameters.getArgNum2();
        //TODO implement processChar10
        try {
            chosenStudent = getStudentsOfCharacter(chosenCharacter, game,studentsIndexes); //gets students placed on character card
            game.playCharacter(chosenCharacter, player,chosenStudent,null,null,null);
            response.setArgString("Character played successfully, student placed in your dining room");
        } catch (NoStudentsException ex) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar11(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        Colour chosenColour = parameters.getStandardArgColour();
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,chosenColour);
            response.setArgString("Character played successfully, your students have been returned to the bag ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    private static LinkedList <Student> getStudentsOfCharacter(Character chosenCharacter, Game game, List parameters) throws NoStudentsException {
        LinkedList <Student>  chosenStudent = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudent.add(chosenCharacter.getStudents().get(i));
        }
        if (chosenCharacter.getStudents().size() == 0) throw new NoStudentsException("There are no students left");
        return chosenStudent;
    }
    private static LinkedList<Student> getStudentsOfEntrance(Character chosenCharacter, Player player, Game game, List parameters) throws NoStudentsException {
        LinkedList<Student> chosenStudents = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudents.add(player.getSchool().getEntrance().get(i));
        }
        if (chosenCharacter.getStudents().size() == 0) throw new NoStudentsException("There are no students left");
        return chosenStudents;
    }
    private static LinkedList<Student> getStudentsOfDining(Player player,Game game, List parameters ) throws NoStudentsException {
        LinkedList chosenStudents = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudents.add(player.getSchool().getDining_rooms().get(i));
        }
        //if (chosenCharacter.getStudents().size() == 0) throw new NoStudentsException("There no students left");
        return chosenStudents;
    }
    public static class NoStudentsException extends Exception {
        public NoStudentsException() {}

        public NoStudentsException(String msg) {
            super(msg);
        }
    }


}
