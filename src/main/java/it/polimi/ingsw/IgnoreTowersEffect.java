package it.polimi.ingsw;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class IgnoreTowersEffect extends CharacterDecorator{

    public IgnoreTowersEffect(SimpleCharacter decoratedCharacter) {
        super(decoratedCharacter);
    }

    @Override
    public void effect(ArrayList<Object> args) {
        super.effect(args);

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("src/main/resources/it/polimi/ingsw/island_sizes.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Island island: getDecoratedCharacter().getGame().getArchipelago()){
            int value = island.getIsland_size();
            try {
                writer.write(value+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            island.setIsland_size(0);
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void endEffect(){
        super.endEffect();
        try {
            Scanner reader = new Scanner(new File("src/main/resources/it/polimi/ingsw/island_sizes.txt"));
            for(Island island: getDecoratedCharacter().getGame().getArchipelago()){
                island.setIsland_size(reader.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }
}
