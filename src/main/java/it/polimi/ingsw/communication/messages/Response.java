package it.polimi.ingsw.communication.messages;

import it.polimi.ingsw.communication.GamePack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Response {
    private boolean effective;
    private int category;
    private int specification;
    private GamePack status;

    public Response(int code) {
        if(isExistingResponse(code)){
            parseEffective(code);
            code = code%10000;
            parseCategory(code);
            code = code%100;
            parseSpecification(code);
        }
        else{
            setEffective(false);
            setCategory(0);
            setSpecification(0);
        }
    }

    private void parseSpecification(int code) {
        setSpecification(code);
    }

    private void parseCategory(int code) {
        int digit = code / 100;
        setCategory(digit);
    }

    private void parseEffective(int code){
        int digit = code / 10000;
        setEffective(digit == 1);
    }

    private boolean isExistingResponse(int code){
        Integer intCode = code;
        Set<Integer> codes = new HashSet<>();
        try {
            Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/codes.txt"));
            while (reader.hasNextInt()) {
                codes.add(reader.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
        return codes.contains(intCode);
    }

    public boolean isEffective() {
        return this.effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSpecification() {
        return this.specification;
    }

    public void setSpecification(int specification) {
        this.specification = specification;
    }

    public GamePack getStatus() {
        return this.status;
    }

    public void setStatus(GamePack status) {
        this.status = status;
    }
}