package it.polimi.ingsw.communication.messages;

import it.polimi.ingsw.model.Student;

import java.util.List;

public record CharacterInfo(
        int cost,
        String description,
        List<Student> students,
        int noEntry
){
}