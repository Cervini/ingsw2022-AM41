package it.polimi.ingsw.communication.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void constructorTest1(){
        Response message = new Response(0);
        assertFalse(message.isEffective());
        assertEquals(message.getCategory(), 0);
        assertEquals(message.getSpecification(), 0);
    }

    @Test
    void constructorTest2(){
        Response message = new Response(11000);
        assertTrue(message.isEffective());
        assertEquals(message.getCategory(), 10);
        assertEquals(message.getSpecification(), 0);
    }

    @Test
    void constructorTest3(){
        Response message = new Response(13320);
        assertTrue(message.isEffective());
        assertEquals(message.getCategory(), 33);
        assertEquals(message.getSpecification(), 20);
    }

    @Test
    void constructorTest4(){
        Response message = new Response(20000);
        assertFalse(message.isEffective());
        assertEquals(message.getCategory(), 0);
        assertEquals(message.getSpecification(), 0);
    }

    @Test
    void constructorTest5(){
        Response message = new Response(1401);
        assertFalse(message.isEffective());
        assertEquals(message.getCategory(), 0);
        assertEquals(message.getSpecification(), 0);
    }
}