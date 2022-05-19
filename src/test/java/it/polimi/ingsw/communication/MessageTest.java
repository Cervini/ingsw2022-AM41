package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.FromTile;
import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.communication.messages.ToTile;
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
        assertEquals(msg.getArgNum2(), 6);
        assertNull(msg.getArgString());
        assertTrue(msg.isStandard());
    }
}
