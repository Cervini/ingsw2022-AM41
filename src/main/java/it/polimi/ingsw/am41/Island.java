package it.polimi.ingsw.am41;

import java.util.ArrayList;
import java.util.List;

public class Island implements Tile {

    private int island_size; // how many base Island form the island
    private boolean is_mother_nature; // true if Mother Nature is currently on the island
    private boolean is_deny_card; // true if a No Entry Card is present on the island
    private List <Student> students; // list of all the students on the island
    private TowerColour tower = null; // color of the team that controls the island, null if it's no one's

    // constructor creates an empty island of size 1
    public Island(){
        this.island_size = 1;
        this.is_mother_nature = false;
        this.is_deny_card = false;
        this.students = new ArrayList<>();
    }

    @Override
    public void putStudent(Student Student) {
        students.add(Student);
    }

    @Override
    public void removeStudent(Student student) {
        throw new UnsupportedOperationException();

    }

    /**
     * @param player player whose influence is calculated
     * @return influence player has on the island
     */
    private int influence(Player player) {

        // set starting influence at 0
        int player_influence=0;

        int blue = (int) students.stream().filter(s -> s.getColour() == Colour.BLUE ).count();
        int red = (int) students.stream().filter(s -> s.getColour() == Colour.RED ).count();
        int yellow = (int) students.stream().filter(s -> s.getColour() == Colour.YELLOW ).count();
        int pink = (int) students.stream().filter(s -> s.getColour() == Colour.PINK ).count();
        int green = (int) students.stream().filter(s -> s.getColour() == Colour.GREEN ).count();

        // add influence gained from owned professors
        for (Professor p: player.getOwned_professor()){
            switch(p.getColour()){
                case BLUE:
                    player_influence+=blue;
                case GREEN:
                    player_influence+=green;
                case PINK:
                    player_influence+=pink;
                case YELLOW:
                    player_influence+=yellow;
                case RED:
                    player_influence+=red;
            }
        }

        // add influence gained by the presence of towers
        if (player.getTeam() == tower)
            player_influence += this.island_size;

        return player_influence;
    }

    /**
     * @param player player whose capability of conquering the island is misured
     * @param players list of all the players
     * @return true if the player has the most influence on the island
     */
    public boolean canConquer(Player player, ArrayList<Player> players) {

        // get the influence of the conquering player
        int player_influence = influence(player);
        for(Player p: players){
            // if any of the other players has greater influence
            if(influence(p)>player_influence)
                // return false: the player can't conquer the island
                return false;
        }
        // if no one has greater influence the player can conquer the island
        return true;
    }

    /**
     * changes the color of the towers on the island if the player can and does conquer the island
     * @param player player conquering the island
     * @param players list of all the players
     * @throws Exception Player can't conquer the island
     */
    public void conquer(Player player, ArrayList<Player> players) throws Exception {
        if(canConquer(player, players))
            this.tower = player.getTeam();
        else {
            throw new Exception("Player cant conquer the island"); // TODO define better exception
        }
    }

    public int getIsland_size() {
        return island_size;
    }

    public void setIsland_size(int island_size) {
        this.island_size = island_size;
    }

    public boolean getIs_mother_nature() {
        return is_mother_nature;
    }

    public void setIs_mother_nature(boolean is_mother_nature) {
        this.is_mother_nature = is_mother_nature;
    }

    public boolean getIs_deny_card() {
        return is_deny_card;
    }

    public void setIs_deny_card(boolean is_deny_card) {
        this.is_deny_card = is_deny_card;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public TowerColour getTower() {
        return tower;
    }

    public void setTower(TowerColour tower) {
        this.tower = tower;
    }
}
