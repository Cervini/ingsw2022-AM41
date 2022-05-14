package it.polimi.ingsw.communication.messages;

import it.polimi.ingsw.communication.Command;
import it.polimi.ingsw.communication.FromTile;
import it.polimi.ingsw.communication.ToTile;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MessageBase implements Serializable {

    private Command command = Command.NULL;
    private Integer argNum1 = null;
    private Integer argNum2 = null;
    private String argString = null;
    private FromTile from_tile = FromTile.NULL;
    private ToTile to_tile = ToTile.NULL;
    private boolean standard = false; // if true the message has a correct structure

    public MessageBase(String string){
        String[] arguments = string.split("\\W+");
        List<String> args = Arrays.stream(arguments).toList();

        if(args.size()>0)
            this.command = toCommandEnum(args.get(0));

        checkNextArgument(this.command, args);
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

    private ToTile toToTileEnum(String string){
        string = string.toUpperCase();
        try {
            ToTile.valueOf(string);
            return ToTile.valueOf(string);
        } catch (IllegalArgumentException e){
            return ToTile.NULL;
        }
    }

    private FromTile toFromTileEnum(String string){
        string = string.toUpperCase();
        try {
            FromTile.valueOf(string);
            return FromTile.valueOf(string);
        } catch (IllegalArgumentException e){
            return FromTile.NULL;
        }
    }

    private void checkNextArgument(Command command, List<String> args){
        switch (command){
            case NULL -> {
                this.standard = false;
            }
            case LOGIN -> {
                if(args.size()>1) {
                    this.argString = args.get(1);
                    this.standard = true;
                }
                else {
                    System.out.println("Not enough arguments");
                }
            }
            case START, END -> {
                this.standard = true;
                if(args.size()>1)
                    System.out.println("Excess arguments were ignored");
            }
            case CHOOSE, MOVE, PLAY -> {
                if(args.size()>1){
                    try{
                        this.argNum1 = Integer.parseInt(args.get(1));
                        this.standard = true;
                    } catch (NumberFormatException e){
                        System.out.println("Impossible argument");
                    }

                }
            }
            case PLACE -> {
                if(args.size()<5){
                    System.out.println("Not enough arguments");
                } else {
                    this.from_tile = toFromTileEnum(args.get(1));
                    if(this.from_tile == FromTile.NULL)
                        break;
                    try{
                        this.argNum1 = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e){
                        System.out.println("Impossible argument");
                        break;
                    }
                    this.to_tile = toToTileEnum(args.get(3));
                    if(this.to_tile == ToTile.NULL)
                        break;
                    try{
                        this.argNum2 = Integer.parseInt(args.get(4));
                    } catch (NumberFormatException e){
                        System.out.println("Impossible argument");
                        break;
                    }
                    this.standard = true;
                }
            }
            case PING,PONG, STRING -> {
                this.standard = true;
            }
        } // switch end
    }


    public Command getCommand() {
        return command;
    }

    public Integer getArgNum1() {
        return argNum1;
    }

    public Integer getArgNum2() {
        return argNum2;
    }

    public String getArgString() {
        return argString;
    }

    public FromTile getFrom_tile() {
        return from_tile;
    }

    public ToTile getTo_tile() {
        return to_tile;
    }

    public boolean isStandard() {
        return standard;
    }

    public void setArgString(String argString) {
        this.argString = argString;
    }

    public void setStandard(boolean standard) {
        this.standard = standard;
    }

    @Override
    public String toString(){
        if(!this.standard)
            return "invalid command";
        String s = "";
        if((command!=Command.NULL)&&(command!=Command.STRING))
            s=command.toString();
        if(argString!=null)
            s=s+" "+argString;
        if(this.from_tile!=FromTile.NULL)
            s=s+" "+from_tile.toString();
        if(argNum1!=null)
            s=s+" "+argNum1;
        if(this.to_tile!=ToTile.NULL)
            s=s+" "+to_tile.toString();
        if(argNum2!=null)
            s=s+" "+argNum2;
        return s;
    }


}
