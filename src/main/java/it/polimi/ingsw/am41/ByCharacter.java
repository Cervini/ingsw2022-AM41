package it.polimi.ingsw.am41;

public interface ByCharacter {
    public void moveMotherNature(int maxMovement);
    public void moveProfessor(Player player);
    public void moveStudent(Tile fromTile, Tile toTile, Student student);
    public void checkInfluence();
    public void moveDenyCard(Island island, SimpleCharacter character);
}
