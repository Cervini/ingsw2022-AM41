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
    private final LinkedList<Integer> argNum2;
    private String argString = null;
    private FromTile from_tile = FromTile.NULL;
    private ToTile to_tile = ToTile.NULL;
    private final LinkedList<Colour> argColour;
    private boolean standard = false; // if true the message has a correct structure
    private GamePack status = null;

    public Message(String string){
        String[] arguments = string.split("\\W+");
        List<String> args = Arrays.stream(arguments).toList();
        this.argNum2 = new LinkedList<>();
        this.argColour = new LinkedList<>();
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

    private void useCase(List<String> args){
        int size = args.size();
        //check minimum length for USE commands
        if(size<2){
            System.out.println("Not enough arguments (To check how to  use the character use command INFO [character index])");
            return;
        }
        //the first argument after USE must be an integer
        try {
            setArgNum1(Integer.parseInt(args.get(1))); // first argument must be an int
        } catch (NumberFormatException e) {
            System.out.println("Impossible command arguments");
            return; //if the first argument is not an int the command does not follow the standard structure
        }
        //parse the other arguments if there are more
        if(size>2){
            if(size>8){
                //since there aren't commands with more than 8 arguments size is cut at 8
                System.out.println("Excess arguments were ignored");
                size=8;
            }
            try {
                argColour.add(toColourEnum(args.get(2))); //check if the second argument is a Colour enum
                if(args.size()>3){
                    //if the second argument is a color there is no need of other arguments
                    System.out.println("Excess arguments were ignored");
                }
            } catch (IllegalArgumentException a) {
                //if not a colour for every other argument try to read it as a colour or an integer
                for(int i=2; i<size; i++){
                    try{
                        argNum2.add(Integer.parseInt(args.get(i)));
                    } catch (NumberFormatException b) {
                        try{
                            argColour.add(toColourEnum(args.get(i)));
                        } catch (IllegalArgumentException c){
                            System.out.println("Impossible command arguments");
                            return;
                        }
                    }
                }
                //check if the number of arguments is ok
                if(!countCheck())
                    return;
            }
        }
        //standard is set to true
        this.standard = true;
    }

    private boolean countCheck() {
        int intSize = this.argNum2.size();
        int colSize = this.argColour.size();
        if(((intSize==2)||(intSize==1))&&(colSize==0))
            return true;
        return intSize == colSize;
    }

    private void useCaseOld(List<String> args) {
        if(args.size()<2){ //check the minimum number of arguments
            System.out.println("Not enough arguments (To check how to  use the character use command CHARACTER [character index])");
        } else {
            try {
                setArgNum1(Integer.parseInt(args.get(1))); // first argument must be an int
            } catch (NumberFormatException e) {
                return; //if the first argument is not an int the command does not follow the standard structure
            }
            if(args.size()>2){
                try {
                    argColour.add(toColourEnum(args.get(2))); // see if the second argument is a Colour enum
                    if(args.size()>3){
                        //if the second argument is a color there is no need of other arguments
                        System.out.println("Excess arguments were ignored");
                    }
                } catch (IllegalArgumentException a) {
                    // if the second arg is not a colour
                    int size = args.size();
                    if(size>8){
                        // there aren't commands with more than 8 arguments, if found more the command is cut at the 8th
                        System.out.println("Excess arguments were ignored");
                        size=8; //the size of the command is set to 8
                    } else if ((size==5)||(size==7)){
                        // commands with 5 or 7 arguments don't follow the standard command structure
                        System.out.println("Impossible number of arguments");
                        return;
                    }
                    for(int i=2; i<2+(size-2)/2; i++){
                        try{
                            this.argNum2.add(Integer.parseInt(args.get(i)));
                        } catch (NumberFormatException e) {
                            return;
                        }
                    }
                    for(int i=2+(size-2)/2; i<size; i++){
                        try{
                            argColour.add(toColourEnum(args.get(i)));
                        } catch (IllegalArgumentException e){
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
        if(argNum2.size()!=0){
            for(int i=0; i<argNum2.size(); i++){
                s=s+" "+argNum2.get(i);
            }
        }
        return s;
    }

    public void setStatus(GamePack gamePack){
        this.status = gamePack;
    }

    public GamePack getStatus() {
        return status;
    }

    public Colour getStandardArgColour() {
        return argColour.getFirst();
    }

    public Colour getArgColour(int index){
        return argColour.get(index);
    }
}
