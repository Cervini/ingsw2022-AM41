package it.polimi.ingsw.communication.messages;

import it.polimi.ingsw.communication.GamePack;
import it.polimi.ingsw.model.Colour;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Message implements Serializable {

    private Command command = Command.NULL;
    private Integer argNum1 = null;
    private LinkedList<Integer> argNum2 = null;
    private String argString = null;
    private FromTile from_tile = FromTile.NULL;
    private ToTile to_tile = ToTile.NULL;
    private Colour argColour = null;
    private boolean standard = false; // if true the message has a correct structure
    private GamePack status = null;

    public Message(String string){
        String[] arguments = string.split("\\W+");
        List<String> args = Arrays.stream(arguments).toList();
        this.argNum2 = new LinkedList<>();
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

    private Colour toColourEnum(String string) throws IllegalArgumentException{
        string = string.toUpperCase();
        Colour.valueOf(string);
        return Colour.valueOf(string);
    }

    private void checkNextArgument(Command command, List<String> args){
        switch (command){
            case NULL -> this.standard = false;
            case LOGIN -> {
                oneStringCase(args);
            }
            case END, LOGOUT, PING, PONG, STRING  -> {
                noArgumentCase(args);
            }
            case CHOOSE, MOVE, PLAY, START, EFFECT -> {
                oneIntCase(args);
            }
            case PLACE -> {
                placeCase(args);
            }
            case HELP -> {
                printHelp();
            }
            case USE ->{
                useCase(args);
            }
        } //switch end
    }

    private void useCase(List<String> args) {
        if(args.size()<2){
            System.out.println("Not enough arguments (To check how to  use the character use command CHARACTER [character index])");
        } else {
            try {
                setArgNum1(Integer.parseInt(args.get(1))); // first argument must be an int
            } catch (NumberFormatException e) {
                return;
            }
            if(args.size()>2){
                try {
                    argColour = toColourEnum(args.get(2)); // see if the second argument is a Colour enum
                    if(args.size()>3){
                        System.out.println("Excess arguments were ignored");
                    }
                } catch (IllegalArgumentException a) {
                    // if not a colour
                    int size = args.size();
                    if(size>8){
                        System.out.println("Excess arguments were ignored");
                        size=8;
                    } else if ((size==5)||(size==7)){
                        System.out.println("Impossible number of arguments");
                        return;
                    }
                    for(int i=2; i<size; i++){
                        try{
                            this.argNum2.add(Integer.parseInt(args.get(i)));
                        } catch (NumberFormatException e) {
                            return;
                        }
                    }
                    //place all the int arguments into argNum2
                }
            } // end of else
        }
        this.standard = true;
    }

    private void placeCase(List<String> args) {
        if(args.size()<4){
            System.out.println("Not enough arguments");
        } else {
            this.from_tile = toFromTileEnum(args.get(1));
            if(this.from_tile == FromTile.NULL)
                return;
            try{
                setArgNum1((Integer.parseInt(args.get(2))));
            } catch (NumberFormatException e){
                System.out.println("Impossible argument");
                return;
            }
            this.to_tile = toToTileEnum(args.get(3));
            if(this.to_tile == ToTile.NULL)
                return;
            if(this.to_tile == ToTile.DINING){
                this.standard = true;
                if(args.size()>4)
                    System.out.println("Excess arguments were ignored");
                return;
            } else {
                if(args.size()<5)
                    System.out.println("Not enough arguments");
            }
            try{
                setArgNum2((Integer.parseInt(args.get(4))));
            } catch (NumberFormatException e){
                System.out.println("Impossible argument");
                return;
            }
            this.standard = true;
        }
    }

    private void oneIntCase(List<String> args) {
        if(args.size()>1){
            try{
                setArgNum1((Integer.parseInt(args.get(1))));
                this.standard = true;
            } catch (NumberFormatException e){
                System.out.println("Impossible argument");
            }
        } else {
            System.out.println("Not enough arguments");
        }
    }

    private void noArgumentCase(List<String> args) {
        this.standard = true;
        if(args.size()>1)
            System.out.println("Excess arguments were ignored");
    }

    private void oneStringCase(List<String> args) {
        if(args.size()>1) {
            this.argString = args.get(1);
            this.standard = true;
        }
        else {
            System.out.println("Not enough arguments");
        }
    }

    private void printHelp() {
        System.out.println("""
                List of available commands:
                LOGIN [u]: log in with u as username, the username must not be already used by any
                           other connected user and cannot contain spaces.
                LOGOUT: log out and disrupt the match if already in one
                START [x]: start a new game with x players
                PLAY [x]: play character with x as index, indexes are indicated next to the Assistant's attributes.
                PLACE ENTRANCE [x] [DINING/ISLAND] [y]: move the student in the x position (in the school board),\s
                                                        from to either the island number y or the dining room of the \s
                                                        right color.
                MOVE [x]: move mother nature of x positions.
                CHOOSE [x]: choose the cloud from where the students are taken int the final phase of your turn.
                USE [x]: play Character with x index.
                EFFECT [x]: get info of character with x index;
                """);
    }

    public Command getCommand() {
        return command;
    }

    public Integer getArgNum1() {
        return argNum1;
    }

    public Integer getSingleArgNum2() {
        return argNum2.getFirst();
    }

    public LinkedList<Integer> getArgNum2() {
        return argNum2;
    }

    public int getArgNum2(int index) {
        try{
            return argNum2.get(index);
        } catch (IndexOutOfBoundsException e){
            return -1;
        }
    }

    public void setArgNum1(int argNum1) {
        this.argNum1 = argNum1;
    }

    public void setArgNum2(int argNum2) {
        this.argNum2.add(argNum2);
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

    public void setStatus(GamePack gamePack){
        this.status = gamePack;
    }

    public GamePack getStatus() {
        return status;
    }

    public Colour getArgColour() {
        return argColour;
    }
}
