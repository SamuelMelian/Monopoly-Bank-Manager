/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

import java.io.Serializable;

/**
 *
 * @author smbmo
 */
public abstract class MonopolyCode implements Serializable{
    
    private String description;
    private int id;
    protected transient Terminal terminal;
    
    public MonopolyCode(int id, String desc, Terminal terminal){  //Constructor de la case monopolyCode, que generaliza a su vez diferentes clases
        this.id = id;
        this.description = desc;
        this.terminal = terminal;
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder(); 
        s.append("\n").append(description);
    return s.toString();    
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void doOperation(Player player);  //Metodo abtracto que tienen que tener todas las propiedades que heredan de esta
    
    protected boolean confirmationMessage(){  //Todos los mensajes de resumen de operaciones necesitan del mismo para confirmar o cancelar
        int opt;
        do{
            terminal.show("Introduzca %s para ACEPTAR y %s para CANCELAR"+Constants.LINE_ARGS_SPLITTER+0+Constants.LINE_ARGS_SPLITTER+1);
            opt = terminal.read();
        } 
        while ((opt != 0) & (opt != 1));
    if (opt == 1){
        terminal.show("La transaccion ha sido cancelada");
    }
    return opt==0;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
