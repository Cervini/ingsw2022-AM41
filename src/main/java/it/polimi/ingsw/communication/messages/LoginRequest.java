package it.polimi.ingsw.communication.messages;

public class LoginRequest extends MessageBase {
    private String name;

    public LoginRequest(String toString) {
        super(toString);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
