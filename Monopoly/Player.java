/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author smbmo
 */
public class Player implements Serializable {

    private final Color color;
    private final String name;
    private int balance;
    private boolean bankrupt;
    private ArrayList<Property> properties;
    private transient Terminal terminal;
    
    public Player(int id, Terminal terminal){  //Constructor de la clase jugador
        properties = new ArrayList<>();
        this.terminal = terminal;
        color = Color.values()[id];    
        terminal.show("Introduzca el nombre del jugador %s" + Constants.LINE_ARGS_SPLITTER + (id + 1));
        name = terminal.readString();
        balance = Constants.INITIAL_MONEY;
        bankrupt = false;
    }
    
    public boolean pay(int amount, boolean mandatory){  //Metodo para pagos de dinero. En caso de no tener suficiente dinero y ser el pago obligatorio, ejecuta sellActives
        if ((balance - amount) >= 0){
            balance -= amount;
            return true;
        }
        else if (mandatory && thereAreThingsToSell()){
            if (sellActives(amount)){
                pay(amount, true);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    
    private boolean sellActives(int target) {  //Pide al jugador que venda activos hasta llegar al balance necesario. Solo se ejecutara el metodo para pagos obligatorios
        terminal.show("El jugador %s tendra que vender casas e hipotecar sus propiedades hasta que disponga del dinero necesario para pagar"+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(color));
        while ((balance < target) && (thereAreThingsToSell())){
            showOwnedProperties();
            terminal.show("Introduzca un codigo de propiedad");
            boolean owned = false;
            int propertyId = terminal.read();
            if ((propertyId > 0) && (propertyId < Constants.MAX_PROPERTY_ID)){
                for (Property property : properties){
                    if ((property.getId() == propertyId) && (property.hasHouses())){
                        Street street = (Street) property;
                        street.sellHouses();
                        owned = true;
                        break;
                    }
                    else if ((property.getId() == propertyId) && !(property.getMortgaged())){
                        property.mortgageProperty();
                        owned = true;
                        break;
                    }
                    else if ((property.getId() == propertyId) && (property.getMortgaged())){
                        terminal.show("La propiedad ya esta hipotecada");
                        owned = true;
                    }
                }
                if (!owned){
                    terminal.show("La propiedad con el codigo %s no le pertenece"+Constants.LINE_ARGS_SPLITTER+propertyId);
                }
            }
            else{
                terminal.show("No existe ninguna propiedad con ese codigo");
            }
        }
    return balance >= target;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\nColor: %s" + Constants.LINE_ARGS_SPLITTER).append(terminal.translateColor(color));
        s.append("\nNombre: %s" + Constants.LINE_ARGS_SPLITTER).append(name);
        s.append("\nDinero actual: %s" + Constants.LINE_ARGS_SPLITTER).append(balance);
        // No mostramos si esta en bancarrota ya que al inicio de cada turno ningun jugador puede estar en bancarrota ya que habria sido eliminado al final del turno anterior
        s.append("\nPosee las siguientes propiedades:");
        for (Property property : properties){
            s.append(property.toString());
        }
    return s.toString();
    }

    public boolean getBankrupt() {
        return bankrupt;
    }

    private boolean thereAreThingsToSell() {  //Comprueba si quedan propiedades por hipotecar 
        for(Property property : properties){
            if (!property.getMortgaged()){
                return true;
            }   
        }
    return false;
    }
    
    public void traspaseProperties (Player newOwner){    //Traspasa todas sus propiedades a otro jugador o a la banca 
        for (Property property : properties){
            property.setOwner(newOwner);  //Actualizamos el propietario en las propiedades de la partida, siendo null si el propietario es la banca
            if (newOwner != null){
                newOwner.properties.add(property);  //En caso de que la propiedad pase a otro jugador, la incluiremos entre sus propiedades
            }
            else{
                property.setMortgaged(false);   //En caso de que la propiedad pase de nuevo a la banca, esta estara sin hipotecar para permitir su compra de manera adecuada 
            }
        }
    }
    
    public void setBankrupt(){
        bankrupt = true;
    }
    
    public int getBalance(){
        return balance;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.color);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        return this.color.equals(other.color);
    }

    public void addProperty(Property p) {
        properties.add(p);
    }

    public Color getColor() {
        return this.color;
    }
    
    public int getTotalHouses(){  //Devuelve el total de casas que tiene un jugador
        int counter = 0;
        for (Property property : properties){
            if (property instanceof Street street){
                if (street.getBuiltHouses() == 5){
                    counter += 4;
                }
                else{
                    counter += street.getBuiltHouses();
                }
            }
        }
    return counter;
    }
    
    public int getTotalHotels(){  //Devuelve el total de hoteles que tiene un jugador
        int counter = 0;
        for (Property property : properties){
            if (property instanceof Street street){
                if (street.getBuiltHouses() == 5){
                    counter += 1;
                }
            }
        }
    return counter;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    private void showOwnedProperties() {
        terminal.show("Posee las siguientes propiedades:");
        for (Property property : properties){
            terminal.show(property.getId()+"."+property.toString());
        }
    }
    
}
