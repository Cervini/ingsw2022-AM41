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

    /**
     * parses a string to Command enum ignoring upper or lower-case differences
     * @param string argument in String format
     * @return Command enum parsed from string parameter
     */
    private Command toCommandEnum(String string){
        string = string.toUpperCase();
        try {
            Command.valueOf(string);
            return Command.valueOf(string);
        } catch (IllegalArgumentException e){
            return Command.NULL;
        }
    }

    /**
     * parses a string to ToTile enum ignoring upper or lower-case differences
     * @param string argument in String format
     * @return ToTile enum parsed from string parameter
     */
    private ToTile toToTileEnum(String string){
        string = string.toUpperCase();
        try {
            ToTile.valueOf(string);
            return ToTile.valueOf(string);
        } catch (IllegalArgumentException e){
            return ToTile.NULL;
        }
    }

    /**
     * parses a string to FromTile enum ignoring upper or lower-case differences
     * @param string argument in String format
     * @return FromTile enum parsed from string parameter
     */
    private FromTile toFromTileEnum(String string){
        string = string.toUpperCase();
        try {
            FromTile.valueOf(string);
            return FromTile.valueOf(string);
        } catch (IllegalArgumentException e){
            return FromTile.NULL;
        }
    }

    /**
     * parses a string to Colour enum ignoring upper or lower-case differences
     * @param string argument in String format
     * @return FromTile enum parsed from string parameter
     * @throws IllegalArgumentException 'string' parameter is not a colour
     */
    private Colour toColourEnum(String string) throws IllegalArgumentException{
        string = string.toUpperCase();
        Colour.valueOf(string);
        return Colour.valueOf(string);
    }

    /**
     * parses the command string setting all the Message arguments
     * @param command Command enum that defines the instruction standard structure
     * @param args list of arguments for the command
     */
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

    /**
     * parse the USE command structure
     * @param args String to parse
     */
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

    /**
     * @return true if the number of argument of each type follows the standard structure
     */
    private boolean countCheck() {
        int intSize = this.argNum2.size();
        int colSize = this.argColour.size();
        if(((intSize==2)||(intSize==1)||(intSize==4)||(intSize==6))&&(colSize==0))
            return true;
        return intSize == colSize;
    }

    /**
     * parse the PLACE command structure
     * @param args String to parse
     */
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

    /**
     * parse the structure of commands with one (1) int as argument
     * @param args String to parse
     */
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

    /**
     * parse the structure of commands with zero (0) int as argument
     * @param args String to parse
     */
    private void noArgumentCase(List<String> args) {
        this.standard = true;
        if(args.size()>1)
            System.out.println("Excess arguments were ignored");
    }

    /**
     * parse the structure of commands with one (1) String as argument
     * @param args String to parse
     */
    private void oneStringCase(List<String> args) {
        if(args.size()>1) {
            this.argString = args.get(1);
            this.standard = true;
        }
        else {
            System.out.println("Not enough arguments");
        }
    }

    /**
     * prints HELP text block
     */
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
