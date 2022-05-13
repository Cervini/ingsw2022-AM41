package it.polimi.ingsw.communication.messages;

public class LoginRequest extends MessageBase {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
