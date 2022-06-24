package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.FromTile;
import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.communication.messages.ToTile;
import it.polimi.ingsw.model.Colour;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void messageConstructorTest01(){
        String user = "start";
        Message msg = new Message(user);
        assertEquals(msg.getCommand(), Command.START);
    }

    @Test
    public void messageConstructorTest02(){
        String user = "hello";
        Message msg = new Message(user);
        assertEquals(msg.getCommand(), Command.NULL);
    }

    @Test
    public void messageConstructorTest03(){
        String user = "place entrance 4 island 6";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.PLACE);
        assertEquals(msg.getFrom_tile(), FromTile.ENTRANCE);
        assertEquals(msg.getArgNum1(), 4);
        assertEquals(msg.getTo_tile(), ToTile.ISLAND);
        assertEquals(msg.getSingleArgNum2(), 6);
        assertNull(msg.getArgString());
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest04(){
        String user = "use 2";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest05(){
        String user = "use 2 4";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertEquals(msg.getSingleArgNum2(), 4);
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest06(){
        String user = "use 2 blue";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertEquals(msg.getStandardArgColour(), Colour.BLUE);
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest07(){
        String user = "use 2 3 2";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertEquals(msg.getArgNum2(0), 3);
        assertEquals(msg.getArgNum2(1), 2);
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest08(){
        String user = "use 2 3 2 5";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertFalse(msg.isStandard());
    }

    @Test
    void messageConstructorTest09(){
        String user = "use 2 3 2 4 blue green red";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertEquals(msg.getArgNum2(0), 3);
        assertEquals(msg.getArgNum2(1), 2);
        assertEquals(msg.getArgNum2(2), 4);
        assertEquals(msg.getArgColour(0), Colour.BLUE);
        assertEquals(msg.getArgColour(1), Colour.GREEN);
        assertEquals(msg.getArgColour(2), Colour.RED);
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest10(){
        String user = "use 2 3 2 5 2 3";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertFalse(msg.isStandard());
    }

    @Test
    void messageConstructorTest11(){
        String user = "use 2 4 3 Yellow red";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertEquals(msg.getArgNum2(0), 4);
        assertEquals(msg.getArgNum2(1), 3);
        assertEquals(msg.getArgColour(0), Colour.YELLOW);
        assertEquals(msg.getArgColour(1), Colour.RED);
        assertTrue(msg.isStandard());
    }

    @Test
    void messageConstructorTest12(){
        String user = "use 2 4 3 pink Yellow red";
        Message msg = new Message(user);

        assertEquals(msg.getCommand(), Command.USE);
        assertEquals(msg.getArgNum1(), 2);
        assertEquals(msg.getArgNum2(0), 4);
        assertEquals(msg.getArgNum2(1), 3);
        assertEquals(msg.getArgColour(0), Colour.PINK);
        assertEquals(msg.getArgColour(1), Colour.YELLOW);
        assertEquals(msg.getArgColour(2), Colour.RED);
        assertFalse(msg.isStandard());
    }
}
