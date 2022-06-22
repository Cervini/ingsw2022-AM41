package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.GamePack;
import it.polimi.ingsw.communication.messages.Command;
import it.polimi.ingsw.communication.messages.Message;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.controller.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ClientHandler implements Runnable{
    private String username = "new client";

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final List<ClientHandler> clients;
    private boolean playerIsOffline = false;
    private Game game = null;
    public boolean isPlayerFirstMove = false;
    private GamePhase currentGamePhase;
    private boolean onePlayerLeft = false;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clients = clients;
        initializeClientServerConnection(clientSocket);
        //System.out.println("Connection completed!");
    }

    public void initializeClientServerConnection(Socket clientSocket) {
        this.clientSocket= clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Message request;
        try {
            //while(request.getCommand() != Command.END) { // while the message is not an END type message
            while(true) {
                request = (Message) in.readObject();// read object from input stream and cast it into Message
                if(request.getCommand()!=Command.PING) { // if it's not a PING message
                    // parsing of not PING commands
                    Message response = null;// flush output stream
                    // send through output stream the msg in String form
                    //if ( onePlayerLeft ) { response= new Message("Wait for others to join"); response.setArgString("Wait for others to join");}
                   if ((username.equals("new client"))&&(request.getCommand()!=Command.LOGIN)) { // force login as first command
                        response = new Message("string"); // setup response
                        response.setArgString("Must log in before sending any other command\n" +
                                "type LOGIN <username>");
                    } else {
                        // if user has already logged in
                        switch(request.getCommand()){ // process command
                            case LOGIN -> response = LoginController.processLogin(request, this);
                            case LOGOUT -> response = LoginController.processLogout(this);
                            case START -> response = GameController.start(request, this, clients);
                            case PLAY -> response = PlanningController.play(request, this, currentGamePhase);
                            case MOVE -> response = ActionController.move(request, this, currentGamePhase);
                            case PLACE -> response = ActionController.place(request, this, currentGamePhase);
                            case EFFECT -> response = GameController.info(request, this);
                            case CHOOSE -> response = ActionController.choose(request, this, currentGamePhase);
                            case USE -> response = GameController.character(request,this, sameMatchPlayers());
                            case NULL -> response = new Message("NULL");
                        }
                        // end of not PING commands
                    }
                    if(game!=null){
                        updateStatus();
                    }
                    out.writeObject(response); // send through output stream the msg in String form
                    out.flush();
                } else {
                    Message pongResponse = new Message("PONG");
                    out.writeObject(pongResponse);
                    out.flush();// send through output stream the msg in String form
                    break;      // break while loop in case of PING
                }

                /*try {// if this reading is removed the server keeps sending the last message to the clients, flooding the network
                    request = (Message) in.readObject(); // try reading another Message object from input stream
                } catch (ClassNotFoundException e) {
                    System.out.println("Unknown object in input stream");
                } catch (EOFException e){
                    //System.out.println("Unknown object in input stream");
                }*/
            }
            //}
        } catch (SocketException e) {
            try {
                clientSocket.close();
                this.setPlayerIsOffline(true);
                LoginController.processLogout(this);
                //clients.remove(this);
                //System.out.println("Client disconnected, socket closed");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public ObjectInputStream getIn() {
        return in;
    }
    public ObjectOutputStream getOut() {
        return out;
    }
    public List<ClientHandler> getClients() {
        return clients;
    }
    public boolean isAvailable(){
        return game == null;
    }

    public List<ClientHandler> sameMatchPlayers(){
        List<ClientHandler> same = new LinkedList<>();
        for(ClientHandler client: clients){
            // TODO: verificare se OK la modifica fatta .. !client.isPlayerIsOffline()
            if(!client.isPlayerIsOffline() && client.getGame()==this.game){
                same.add(client);
            }
        }
        return same;
    }

    /**
     * packs the game status in a new GamePack then sends the new status to all the playing users
     */
    public void updateStatus(){
        Message status = new Message("status");
        status.setStandard(true);
        GamePack gamePack = new GamePack(game, this);
        for(ClientHandler clientHandler: sameMatchPlayers()){
            //Send Game status if the client is currently in a game
            gamePack.updateGamePack(game, clientHandler);
            status.setStatus(gamePack);
            try {
                clientHandler.getOut().writeObject(status);
                clientHandler.getOut().flush();
                clientHandler.getOut().reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*public String onePlayerLeft(ClientHandler player) throws IOException {
        onePlayerLeft = true;
        String check = "You can continue the game";
        final Duration timeout = Duration.ofSeconds(20);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        final Future<String> handler = executor.submit(new Callable() {
            @Override
            public String call() throws Exception {

                String check = checkNumPlayers(player);
                // notificare unico giocatore rimasto che lui e' il vincitore
                // TODO: marcare il game ended = true
                // TODO: bloccare tutte le mosse per unico giocatore rimasto (validazione nella fase del gioco)
                return check;
            }
        } );

        try {
            handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            if (player.sameMatchPlayers().size() == 1) {
                gameEndedWithOnePlayer(player);
                check = "Game ended because all players left, you are the winner! \n You can now begin a new match!";
            }
            else { return check; }
        }

        executor.shutdownNow();
        //player.getCurrentGamePhase().setGameEnded(true);
        onePlayerLeft = false;
        return check;

    }*/

    public boolean getPlayerFirstMove() {
        return isPlayerFirstMove;
    }

    public void setPlayerFirstMove(boolean playerFirstMove) {
        this.isPlayerFirstMove = playerFirstMove;
    }

    public void setCurrentGamePhase(GamePhase gamePhase) {
        this.currentGamePhase = gamePhase;
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    public String getPhase() {
        if(currentGamePhase.isPlanningPhase())
            return "PLANNING PHASE";
        else {
            return game.getActivePlayer().getPlayer_id();
        }
    }

     public boolean isPlayerIsOffline() {
        return playerIsOffline;
    }

      public void setPlayerIsOffline(boolean playerIsOffline) {
        this.playerIsOffline = playerIsOffline;
    }


   /*public String checkNumPlayers(ClientHandler player){
        while(player.sameMatchPlayers().size() == 1){continue;};
        return null;
    }
    private void gameEndedWithOnePlayer(ClientHandler player) throws IOException {
        for ( ClientHandler c : player.clients ) {
            if(c.isPlayerIsOffline()) sameMatchPlayers().remove(c);
            c.setGame(null);
        }
    }*/


}