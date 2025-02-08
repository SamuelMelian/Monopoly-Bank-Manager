/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.util.ArrayList;

/**
 *
 * @author smbmo
 */
public abstract class Property extends MonopolyCode{
    
    private Player owner;
    private final int price;
    private boolean mortgaged;
    private final int mortgageValue;
    
    
    public Property(int id, String desc, Terminal terminal,int price, boolean mortgaged, int mValue){
        super(id,desc,terminal);
        this.price = price;
        this.mortgaged = mortgaged;
        this.mortgageValue = mValue; 
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(super.toString());
        s.append("\nHipotecada: ");
        if (mortgaged){
            s.append("si");
        }
        else{
            s.append("no");
        }
    return s.toString();
    }
    
    public abstract void doOwnerOperations();
    
    protected Player getOwner(){
        return owner;
    } 

    protected int getPrice() {
        return price;
    }

    public void setOwner(Player player) {
        owner = player;
    }
    
    private void showPurchaseSummary(Player player) {
        terminal.show("Se va a realizar la compra de la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+price);
    }
    
    private void showMortgageManagementSummary(int amount, Player player) {  //Se muestra el mensaje en funcion de si esta hipotecada o no
        if (!mortgaged){
            terminal.show("Se va a hipotecar la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
        }
        else{
            terminal.show("Se va a deshipotecar la propiedad %s por parte del jugador %s por un importe de %s. [ACEPTAR/CANCELAR]"+Constants.LINE_ARGS_SPLITTER+this.getDescription()+Constants.LINE_ARGS_SPLITTER+terminal.translateColor(player.getColor())+Constants.LINE_ARGS_SPLITTER+amount);
        }
    }
    
    protected void purchaseProperty(Player player){
        showPurchaseSummary(player);
        if(confirmationMessage()){
            if (player.pay(this.getPrice(),false)){
                this.setOwner(player);
                player.addProperty(this);
                terminal.show("Compra realizada con exito");
            }
            else{
                terminal.show("No se ha podido realizar la compra ya que no dispone de suficiente dinero");
            }
        }
    }
    
    protected void mortgageProperty() {
        if (mortgaged){
            terminal.show("No se puede hipotecar una propiedad que ya esta hipotecada");
        }
        else if (this.hasHouses()){
            terminal.show("No se puede hipotecar una propiedad con casas construidas");
        }
        else{
            int amount = mortgageValue;
            showMortgageManagementSummary(amount,owner);
            if (confirmationMessage()){
                owner.pay(-amount,false);
                mortgaged = true;
                terminal.show("La propiedad se ha hipotecado con exito");
            }
        }
    }

    protected void unmortgageProperty() {
        if (mortgaged){
            int amount = mortgageValue/10 + mortgageValue;  //Segun las reglas, el precio para deshipotecar una propiedad es el valor de hipoteca mas el 10% de intereses
            showMortgageManagementSummary(amount,owner);
            if (confirmationMessage()){
                if(owner.pay(amount,false)){
                    mortgaged = false;
                    terminal.show("La propiedad se ha deshipotecado con exito");
                }
                else{
                    terminal.show("No se ha podido deshipotecar la propiedad ya que no dispone de suficiente dinero");
                }
            }
        }
        else{
            terminal.show("No se puede deshipotecar una propiedad que no esta hipotecada");
        }
    }
    
    public abstract int getPaymentForRent();
    
    public abstract void showPaymentSummary(int amount, Player player);
    
    protected int getPropertiesOfThisClass(){  //Devuelve el numero de propiedades que son de la misma clase que aquella desde la que se llamo al metodo
        int objects = 0;
        ArrayList<Property> properties = owner.getProperties();
        for (Property property : properties){
            if (property.getClass() == this.getClass()){
                objects += 1;
            }
        }
    return objects;
    }

    public boolean hasHouses() {
        if (this instanceof Street street){
            return (street.getBuiltHouses()>0);
        }
        else{
            return false;
        }
    }

    public boolean getMortgaged() {
        return mortgaged;
    }
    
    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }
    
    @Override
    public void doOperation(Player player) {  //Todas las clases que heredan de ella comparten la misma implementacion del metodo, lo que cambia es doOwnerOperations
        if (owner == null){
            this.purchaseProperty(player);
        }
        else if (owner.equals(player)){
            doOwnerOperations();
        }
        else if (!mortgaged){
            int payment = getPaymentForRent();
            showPaymentSummary(payment, player);
            if (confirmationMessage()){
                if(player.pay(payment, true)){
                    owner.pay(-payment, true);
                }
                else{
                    terminal.show("El jugador %s ha quedado en bancarrota. Todo su dinero y propiedades restantes pasaran al jugador %s"+ Constants.LINE_ARGS_SPLITTER +terminal.translateColor(player.getColor())+ Constants.LINE_ARGS_SPLITTER +terminal.translateColor(owner.getColor()));
                    owner.pay(-player.getBalance(),true);
                    player.traspaseProperties(owner);
                    player.setBankrupt();
                }
            }
        }
    }

}
