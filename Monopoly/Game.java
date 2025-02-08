/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import static java.lang.System.exit;

/**
 *
 * @author smbmo
 */
public class Game implements Serializable{
    
    private transient Terminal terminal;
    private final MonopolyCode[] monopolyCodeArray;
    private Player[] players;
    
    public Game(Terminal terminal){  //Constructor de game
        this.terminal = terminal;
        monopolyCodeArray = new MonopolyCode[Constants.MAX_MONOPOLYCODE];
    }
    
    private void loadMonopolyCodes(){  //Se encarga de leer el fichero de configuracion y crear todas las tarjetas correspondientes
        try{
            Reader in = new FileReader(Constants.FILEPATH_MONOPOLYCODE);
            BufferedReader buf = new BufferedReader(in,10);
            String s = buf.readLine();
            MonopolyCode mCode;
            while (s != null){
                String[] values = s.split(Constants.MONOPOLY_CODE_SPLITTER);
                mCode = switch (values[1]) {
                    case "STREET" -> new Street(values,terminal);
                    case "PAYMENT_CHARGE_CARD" -> new PaymentCharge(values,terminal);
                    case "TRANSPORT" -> new Transport(values,terminal);
                    case "SERVICE" -> new Service(values,terminal);
                    case "REPAIRS_CARD" -> new RepairsCard(values,terminal);
                    default -> null;
                };
                if ((mCode != null) && (mCode.getId() > 0) &&(mCode.getId() < Constants.MAX_MONOPOLYCODE)){
                    monopolyCodeArray[mCode.getId()] = mCode;
                }
            s = buf.readLine();
            } 
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createPlayers() {  //Pregunta el numero de jugadores y los introduce en la partida
        int i;
        do{
            terminal.show("Introduzca el numero de jugadores de la partida (entre %s y %s)"+Constants.LINE_ARGS_SPLITTER+Constants.MIN_PLAYERS+Constants.LINE_ARGS_SPLITTER+Constants.MAX_PLAYERS);
            i = terminal.read();
            if (i >= Constants.MIN_PLAYERS && i <= Constants.MAX_PLAYERS){
                players = new Player[i];
                for (int cont = 0; cont < i; cont++){
                    players[cont] = new Player(cont, terminal);
                }  
            }
            else{
                terminal.show("El numero de jugadores tiene que estar entre %s y %s"+Constants.LINE_ARGS_SPLITTER+Constants.MIN_PLAYERS+Constants.LINE_ARGS_SPLITTER+Constants.MAX_PLAYERS);
            }
        }
        while (!(i >= Constants.MIN_PLAYERS && i <= Constants.MAX_PLAYERS));
    }

    public void play(String fileName) { //Secuencia de una partida, que recibe el nombre del fichero de la partida para ir serializandola para cada turno y asi estar sujeta a caidas del sistema
        terminal.show("Iniciando partida...");
        while (gameContinues()){
            terminal.show("%s.Mostrar el estado de la partida"+Constants.LINE_ARGS_SPLITTER+0+"\n%s.Introducir un codigo de tarjeta"+Constants.LINE_ARGS_SPLITTER+1+"\n%s.Guardar y salir"+Constants.LINE_ARGS_SPLITTER+2);
            terminal.show("Introduzca numero de opcion");
            int opt = terminal.read();
            if (opt == 1){
                terminal.show("Introduzca codigo de tarjeta");
                int monopolyCodeId = terminal.read();
                MonopolyCode monopolyCode = null; 
                if (monopolyCodeId < Constants.MAX_MONOPOLYCODE){
                    monopolyCode = monopolyCodeArray[monopolyCodeId];
                }
                Player player = askForPlayerCode();
                if (monopolyCode != null){
                    monopolyCode.doOperation(player);
                    if (player.getBankrupt()){
                        removePlayer(player);
                    }
                    serialize(fileName);
                }
                else{
                    terminal.show("No existe ninguna tarjeta con ese identificador");
                }
            }
            else if (opt == 0){
                terminal.show(this.toString(fileName));
            }
            else if (opt == 2){
                serialize(fileName);
                System.exit(0);
            }
        }
        showWinnerMessage();
    }

    public void initialize() {  // Generaliza lo que hay que hacer para inicializar una partida nueva
        this.createPlayers();
        this.loadMonopolyCodes();
        terminal.show("Creando partida...");
    }

    public void setMonopolyCode(int id, MonopolyCode mCode) {
        monopolyCodeArray[id] = mCode; 
    }

    private boolean gameContinues() {  //Comprueba que la partida continua (que quedan 2 o más jugadores vivos)
        int cont = 0;
        for (Player p : players){
            if (!(p == null || p.getBankrupt())){
                cont++;
            }  
        }
        return cont > 1;
    }

    void serialize(String fileName) {  //Metodo que serlializa la partida dado el nombre de fichero de la misma
        try {
            ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(Constants.FILEPATH_OLDGAMES + fileName + ".obj"));
            ous.writeObject(this);
            ous.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removePlayer(Player player) {  //Permite eliminar a un jugador de la partida una vez ha quedado en bancarrota
        for (int i = 0; i < players.length ; i++){
            if ((players[i] != null) && (players[i].equals(player))){
                players[i] = null;
            }
        }
    }

    public String toString(String fileName) {  //El metodo toString() devuelve el string sin traducir, por lo que necesitaremos codificar el texto y las variables por cada linea para despues poder traducir correctamente. Para ello utilizaremos un separador, en este caso "//"
        StringBuilder s = new StringBuilder();
        s.append("Estado de la partida '%s'" + Constants.LINE_ARGS_SPLITTER).append(fileName.replaceFirst("\\.obj$",""));
        for (int i = 0; i < players.length; i++){
            if (players[i] != null){
                s.append("\nJugador %s" + Constants.LINE_ARGS_SPLITTER).append(i+1).append(players[i].toString());
            }
        }
    return s.toString();
    }

    public void setTerminal(Terminal terminal) {  //Tras haber creado o cargado una partida, como no se serializa la terminal tenemos que hacer un set en 
        this.terminal = terminal;
        for (MonopolyCode monopolyCode : monopolyCodeArray){
            if (monopolyCode != null){
                monopolyCode.setTerminal(terminal);
            }
        }
        for (Player player : players){
            if (player != null){
                player.setTerminal(terminal);
            }
        }
    }

    private void showWinnerMessage() {  //Muestra por pantalla quien es el jugador ganador de la partida cuando solo queda 1
        for (Player player : players){
            if (player != null){
                terminal.show("El jugador %s ha ganado la partida. Pulse cualquier numero para salir del programa"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())); 
            }
        }
        terminal.read();
    }

    private Player askForPlayerCode() {  //En cada turno, el metodo permite preguntar al usuario el codigo de jugador (ademas muestra por pantalla los jugadores vivos)
        int id;
        do{
            StringBuilder s = new StringBuilder();
            s.append("Introduzca codigo de jugador (%s)"+Constants.LINE_ARGS_SPLITTER);
            for (int i = 0; i < players.length-1; i++){
                if (players[i] != null){
                    s.append(i).append(" = ").append(terminal.translateColor(players[i].getColor())).append(", ");
                }
            }
            if (players[players.length-1] != null){
                s.append(players.length-1).append(" = ").append(terminal.translateColor(players[players.length-1].getColor()));
            }
            terminal.show(s.toString());
            id = terminal.read();
            }
            while(!((id >= 0) && (id < players.length) && (players[id] != null)));
        return players[id];
    }
}
