package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void messageConstructorTest01(){
        String user = "control";
        Message msg = new Message(user);

        assertEquals(msg.getType(),Type.CONTROL);
        assertEquals(msg.getCommand(), Command.NULL);
    }

    @Test
    public void messageConstructorTest02(){
        String user = "hello";
        Message msg = new Message(user);

        assertEquals(msg.getType(), Type.NULL);
        assertEquals(msg.getCommand(), Command.NULL);
    }
}
