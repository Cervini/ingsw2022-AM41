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
            chosenStudent = getStudentsFromCharacter(chosenCharacter,studentsIndexes); //gets students placed on character card
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
        if (player.getFace_up_assistant()==null) throw new Exception("You have to play an assistant before!");
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
            LinkedList<Student> studentsFromCharacter = getStudentsFromCharacter(chosenCharacter,studentsFromCharacterIndexes);
            LinkedList<Student> studentsFromEntrance = getStudentsFromEntrance(player, studentsFromEntranceIndexes);
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
        if(chosenCharacter==null)  throw new Exception("You have to choose a colour");
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
        List<Integer> studentsFromEntranceIndexes = parameters.getArgNum2();
        LinkedList<Student> chosenStudentsFromEntrance = getStudentsFromEntrance(player,studentsFromEntranceIndexes);
        if (chosenStudentsFromEntrance.size() < studentsFromEntranceIndexes.size()){ response.setArgString("Not enough students in your entrance"); return response;}
        try {
            LinkedList<Student> chosenStudentsFromDining = getStudentsFromDining(player,parameters);
            game.playCharacter(chosenCharacter, player,chosenStudentsFromEntrance,chosenStudentsFromDining,null,null);
            response.setArgString("Character played successfully, students have been exchanged!");
        } catch (Exception e) {
            throw new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    public static Message processChar10(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");
        LinkedList<Student> chosenStudent = null;
        List<Integer> studentsIndexes = parameters.getArgNum2();
        try {
            chosenStudent = getStudentsFromCharacter(chosenCharacter,studentsIndexes); //gets students placed on character card
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

    private static LinkedList <Student> getStudentsFromCharacter(Character chosenCharacter, List parameters) throws NoStudentsException {
        LinkedList <Student>  chosenStudent = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudent.add(chosenCharacter.getStudents().get(i));
        }
        if (chosenStudent.size()<numberOfStudents) throw new NoStudentsException("There are no students left");
        return chosenStudent;
    }
    private static LinkedList<Student> getStudentsFromEntrance(Player player, List parameters) throws NoStudentsException {
        LinkedList<Student> chosenStudents = new LinkedList<>();
        int numberOfStudents = parameters.size(); //number of chosen students
        for (int i = 0; i < numberOfStudents; i++) {
            chosenStudents.add(player.getSchool().getEntrance().get(i));
        }
        return chosenStudents;
    }
    private static LinkedList<Student> getStudentsFromDining(Player player, Message parameters ) throws NoStudentsException {
        LinkedList<Student> chosenStudents = new LinkedList<>();
        Colour firstColour = parameters.getArgColour(0);
        Colour secondColour = parameters.getArgColour(1);
        if( player.getSchool().getDining_room(firstColour).getStudents() == 0 && player.getSchool().getDining_room(secondColour).getStudents() == 0) {
            throw new NoStudentsException("There are no students of chosen colour");}
        Student firstStudent = new Student(firstColour);
        chosenStudents.add(firstStudent);
        Student secondStudent = new Student(secondColour);
        chosenStudents.add(secondStudent);
        return chosenStudents;
    }
    public static class NoStudentsException extends Exception {
        public NoStudentsException() {}

        public NoStudentsException(String msg) {
            super(msg);
        }
    }


}