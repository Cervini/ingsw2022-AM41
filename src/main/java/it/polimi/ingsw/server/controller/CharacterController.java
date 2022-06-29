package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterController {

    /**
     *collects one student(chosen by the player) placed on character 0 and puts it on an island (chosen by the player)
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar0(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        LinkedList<Student> chosenStudent = new LinkedList<>();//creates a new list in which students chosen by the player are collected
        int indexOfChosenStudent = parameters.getArgNum2(0);//gets the student's index from client's request
        if( indexOfChosenStudent >= chosenCharacter.getStudents().size() ) throw new Exception("Not existing student, please retry");//non existent student
        chosenStudent.add(chosenCharacter.getStudents().get(indexOfChosenStudent)); //add chosen student
        try {
            int islandNumber = parameters.getArgNum2(1); //gets the island's index from client's request
            if(islandNumber >= game.getArchipelago().size()) throw new Exception("Not existing island, please retry");//non existent island
            Island chosenIsland = game.getArchipelago().get(islandNumber); //gets chosen island
            response.setArgString("Character played successfully, student placed on island "+islandNumber);//response
            game.playCharacter(chosenCharacter, player,chosenStudent,null,chosenIsland,null);//calls model method
        } catch (NoStudentsException ex) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            throw new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar1(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        try {
            response.setArgString("Character played successfully, during this turn influence will be computed as if you had taken control of all professors");
            game.playCharacter(chosenCharacter, player,null,null,null,null);//calls model method
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     * picks an island as if mother nature were on it and computes influence
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar2(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        int islandNumber = parameters.getSingleArgNum2();//gets the island's index from client's request
        if( islandNumber >= game.getArchipelago().size()) //non existent island
            throw new Exception("Not existing island, please retry");
        Island chosenIsland = game.getArchipelago().get(islandNumber);//gets chosen island
        try {
            response.setArgString("Character played successfully, mother nature has been placed on island "+islandNumber);//calls model method
            game.playCharacter(chosenCharacter, player,null,null,chosenIsland,null);
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *calls model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar3(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        if (player.getFace_up_assistant()==null) throw new Exception("You have to play an assistant before!");
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);//calls model method
            response.setArgString("Character played successfully, you may move Mother Nature up to 2 additional Islands");//sets server response
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *picks an island (chosen by an index) and calls model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar4(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        int islandNumber = parameters.getSingleArgNum2();//gets the island's index from client's request
        if( islandNumber >= game.getArchipelago().size())//non existent island
            throw new Exception("Not existing island, please retry");
        Island chosenIsland = game.getArchipelago().get(islandNumber);//gets chosen island
        try {
            response.setArgString("Character played successfully, no-entry tile has been placed on island "+islandNumber);//sets server response
            game.playCharacter(chosenCharacter, player,null,null,chosenIsland,null);//calls model method
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;

    }

    /**
     *calls model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar5(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);//calls model method
            response.setArgString("Character played successfully, next time mother nature will move towers will not count towards influence ");//sets server response
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *picks 3 students placed on character and 3 students placed in schoolboard entrance and exchanges them
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar6(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        try {
            List<Integer> studentsFromCharacterIndexes = parameters.getArgNum2().stream().limit(3).collect(Collectors.toList());//collects students indexes from client's request
            List<Integer> studentsFromEntranceIndexes = parameters.getArgNum2().subList(Math.max(parameters.getArgNum2().size() - 3, 0), parameters.getArgNum2().size());//collects students indexes from client's request
            LinkedList<Student> studentsFromCharacter = getStudentsFromCharacter(chosenCharacter,studentsFromCharacterIndexes);//collect students placed on character
            LinkedList<Student> studentsFromEntrance = getStudentsFromEntrance(player, studentsFromEntranceIndexes);//collect students placed in schoolboard entrance
            game.playCharacter(chosenCharacter, player,studentsFromCharacter,studentsFromEntrance,null,null);
            response.setArgString("Character played successfully, students have been placed in your School board Entrance");
        } catch (NoStudentsException ex ) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *calls model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar7(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,null);
            response.setArgString("Character played successfully, you have 2 additional influence points ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     * parses colour chosen by the player and calls model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar8(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        Colour chosenColour = parameters.getStandardArgColour();//parses chosen colour
        if(chosenCharacter == null)  throw new Exception("You have to choose a colour");
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,chosenColour);
            response.setArgString("Character played successfully, this colour will add no influence ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *collects 2 students from schoolboard entrance and 2 students from dining room and exchanges them
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar9(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        List<Integer> studentsFromEntranceIndexes = parameters.getArgNum2();//collects students indexes from client's request
        LinkedList<Student> chosenStudentsFromEntrance = getStudentsFromEntrance(player,studentsFromEntranceIndexes);//collects students placed in schoolboard entrance
        if (chosenStudentsFromEntrance.size() < studentsFromEntranceIndexes.size()){ response.setArgString("Not enough students in your entrance"); return response;}
        try {
            LinkedList<Student> chosenStudentsFromDining = getStudentsFromDining(player,parameters);//get students whose colour has been chosen by the player
            game.playCharacter(chosenCharacter, player,chosenStudentsFromEntrance,chosenStudentsFromDining,null,null);
            response.setArgString("Character played successfully, students have been exchanged!");
        } catch (Exception e) {
            throw new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *collects one students placed on this character and calls model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar10(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        LinkedList<Student> chosenStudent = new LinkedList<>();//new list to collect chosen students
        int indexOfChosenStudent = parameters.getArgNum2(0);//collects students indexes from client's request
        if( indexOfChosenStudent >= chosenCharacter.getStudents().size() ) throw new Exception("Not existing student, please retry");
        chosenStudent.add(chosenCharacter.getStudents().get(indexOfChosenStudent));//collects chosen students in a list
        try {
            game.playCharacter(chosenCharacter, player,chosenStudent,null,null,null);
            response.setArgString("Character played successfully, student placed in your dining room");
        } catch (NoStudentsException ex) {
            response.setArgString("No students on this card");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *parses colour chosen by the player and call model method
     * @param player player who sent the command
     * @param parameters related to character usage
     * @param game current game
     * @param chosenCharacter chosen by the player
     * @return message containing the action result
     * @throws Exception thrown in case player has not enough coins to play this character
     */
    public static Message processChar11(Player player, Message parameters, Game game, Character chosenCharacter) throws Exception {
        Message response = new Message("string");// set up output message
        Colour chosenColour = parameters.getStandardArgColour();//parses colour chosen by the player
        try {
            game.playCharacter(chosenCharacter, player,null,null,null,chosenColour);
            response.setArgString("Character played successfully, your students have been returned to the bag ");
        } catch (Exception e) {
            throw  new Exception(e.getMessage()); // "Not enough coins" exception
        }
        return response;
    }

    /**
     *collects students placed on character card
     * @param chosenCharacter chosen by the player
     * @param parameters related to character usage
     * @return message containing the action result
     * @throws Exception thrown if there no students left
     */
    private static LinkedList <Student> getStudentsFromCharacter(Character chosenCharacter, List<Integer> parameters) throws Exception {
        try {
            LinkedList <Student>  chosenStudents = new LinkedList<>();
            parameters.forEach( i -> chosenStudents.add(chosenCharacter.getStudents().get(i)));//for each student's index gets relevant student
            if (chosenStudents.size() < parameters.size()) throw new NoStudentsException("There are no students left");
            return chosenStudents;
        }catch (IndexOutOfBoundsException e){
            throw new Exception("There is no such student");
        }
    }

    /**
     *collects students placed in player's entrance
     * @param parameters related to character usage
     * @return message containing the action result
     * @throws Exception thrown if there are no students left
     */
    private static LinkedList<Student> getStudentsFromEntrance(Player player, List<Integer> parameters) throws Exception {
        LinkedList<Student> chosenStudents = new LinkedList<>();
        try {
        parameters.forEach( i -> chosenStudents.add(player.getSchool().getEntrance().get(i)));//for each student's index gets relevant student
        if (chosenStudents.size()<parameters.size()) throw new NoStudentsException("There are no students left");
        return chosenStudents;
        } catch (IndexOutOfBoundsException e){
            throw new Exception("There is/are no such student/s");
        }
    }

    /**
     *
     * @param player who sent the command
     * @param parameters related to character usage
     * @return message containing the action result
     * @throws NoStudentsException thrown if there are no students left
     */
    private static LinkedList<Student> getStudentsFromDining(Player player, Message parameters ) throws NoStudentsException {
        LinkedList<Student> chosenStudents = new LinkedList<>();
        Colour firstColour = parameters.getArgColour(0); //first chosen colour
        Colour secondColour = parameters.getArgColour(1);//second chosen colour
        if( player.getSchool().getDining_room(firstColour).getStudents() == 0 && player.getSchool().getDining_room(secondColour).getStudents() == 0) {
            throw new NoStudentsException("There are no students of chosen colour");}// thrown if there no students of chosen colours
        Student firstStudent = new Student(firstColour);//creates new student of first colour
        chosenStudents.add(firstStudent);//add it to chosen students list
        Student secondStudent = new Student(secondColour);//creates new student of second colour
        chosenStudents.add(secondStudent);//add it to chosen students list
        return chosenStudents;
    }

    public static class NoStudentsException extends Exception {
        public NoStudentsException() {}

        public NoStudentsException(String msg) {
            super(msg);
        }
    }

}