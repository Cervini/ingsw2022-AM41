package it.polimi.ingsw.communication;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Message implements Serializable{
    private Type type = Type.NULL;
    private Command command = Command.NULL;

    public Message(String string){
        String[] arguments = string.split("\\W+");
        List<String> args = Arrays.stream(arguments).toList();

        if(args.size()>0)
            this.type = toTypeEnum(args.get(0));
        if(args.size()>1)
            this.command = toCommandEnum(args.get(1));
    }

    private Type toTypeEnum(String string){
        string = string.toUpperCase();
        try {
            Type.valueOf(string);
            return Type.valueOf(string);
        } catch (IllegalArgumentException e){
            return Type.NULL;
        }
    }

    private Command toCommandEnum(String string){
        string = string.toUpperCase();
        try {
            Command.valueOf(string);
            return Command.valueOf(string);
        } catch (IllegalArgumentException e){
            return Command.NULL;
        }
    }

    @Override
    public String toString(){
        return getType().toString() + " " + getCommand().toString();
    }

    public Type getType() {
        return type;
    }

    public Command getCommand() {
        return command;
    }
}
