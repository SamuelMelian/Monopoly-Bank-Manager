/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Monopoly;

/**
 *
 * @author smbmo
 */
public abstract class Terminal{
    
    private TranslatorManager translatorManager;
    
    public Terminal(){  //Constructor de una terminal
        translatorManager = new TranslatorManager();
    }
    public TranslatorManager getTranslatorManager(){
        return translatorManager;
    }
    public abstract int read();  //Todos son metodos abstractos que tendria que implementar cualquier terminal
    public abstract void show(String s);
    public abstract String readString();
    public abstract String translateColor(Color color);
    
}
