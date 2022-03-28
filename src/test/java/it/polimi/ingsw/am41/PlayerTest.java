package it.polimi.ingsw.am41;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

class PlayerTest {
    // ..\\..\\..\\..\\..\\..\\main\\resources\\it\\polimi\\ingsw\\am41\\assistants_stats.txt
    @Test
    void testConstructor(){
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        System.out.println();
        Player player = new Player(Tower_color.black);
        for (Assistant assistant : player.getAssistants()){
            System.out.println("\ninitiative: " + assistant.getValue() +
                    "\nmoves: " + assistant.getMovement_points());
        }
    }

}